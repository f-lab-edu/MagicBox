# MagicBox Backend Codebase 분석

## 1. 프로젝트 개요

MagicBox는 마술사(크리에이터)와 일반 사용자를 연결하는 플랫폼의 백엔드 시스템이다. Spring Boot 4.0 + Java 21 기반의 멀티모듈 Gradle 프로젝트로, 10개의 마이크로서비스로 구성되어 있다.

| 항목 | 스택 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Build | Gradle (멀티모듈) |
| DB | MySQL 8.0, Redis 7 |
| Messaging | Apache Kafka 3.7 + Debezium CDC (Outbox Pattern) |
| IPC | gRPC (Spring gRPC 1.0.2 + io.grpc 1.79) |
| Resilience | Resilience4j Circuit Breaker |
| Auth | OAuth2 (Google, Naver, Kakao) + JWT |
| Infra | Kubernetes (dev), Docker Compose (local) |
| CI/CD | GitHub Actions (PR Validation + SonarCloud) |
| ID 생성 | Sonyflake (분산 ID) |

---

## 2. 아키텍처

### 2.1 Hexagonal Architecture (Ports & Adapters)

모든 서비스가 일관된 헥사고날 아키텍처를 따른다.

```
adapter/
  in/                       ← Driving Adapters (외부 → 시스템)
    web/                    ← REST Controller
    grpc/                   ← gRPC Server
    kafka/                  ← Kafka Consumer
    security/               ← Security Filter Chain
  out/                      ← Driven Adapters (시스템 → 외부)
    persistence/            ← JPA Repository
    cache/                  ← Redis (auth만)
    communication/grpc/     ← gRPC Client
    jwt/                    ← JWT 발급/검증 (auth만)
application/
  port/
    in/                     ← UseCase 인터페이스 (Driving Port)
    out/                    ← Repository/External Port (Driven Port)
  service/                  ← UseCase 구현체
  dto/
    command/                ← 쓰기 요청 DTO
    query/                  ← 읽기 요청 DTO
    result/                 ← 응답 DTO
domain/
  aggregate/                ← Aggregate Root
  vo/                       ← Value Objects
  enums/                    ← 도메인 열거형
  event/                    ← 도메인 이벤트
  exception/                ← 도메인 예외
  constants/                ← 도메인 정책 상수
  service/                  ← 도메인 서비스 (필요 시)
global/
  exception/                ← BaseException, BusinessException, SystemError
  configuration/            ← Properties 등록
```

### 2.2 CQRS 분리

컨트롤러 레벨에서 Command/Query를 명시적으로 분리한다.
- `*CommandController` : POST, PATCH, DELETE (상태 변경)
- `*QueryController` : GET (조회)
- `Admin*Controller` : 관리자 전용 엔드포인트

UseCase도 1인터페이스 1메서드 원칙으로 분리되어 있다.
- `RegisterGeneralGoodsUseCase`, `DeleteGeneralGoodsUseCase` 등 행위별 개별 인터페이스

### 2.3 이벤트 기반 통신 (Transactional Outbox Pattern + Debezium CDC)

서비스 간 비동기 통신에 Transactional Outbox Pattern을 사용한다.

```
[Service] → DB INSERT (domain_event 테이블)
         → Debezium CDC가 binlog 감지
         → Kafka 토픽으로 자동 발행
         → [Consumer Service] Kafka Listener에서 수신
```

- 도메인 이벤트는 `*DomainEvent` 엔티티로 영속화
- Debezium EventRouter가 `event_type` 필드 기반으로 토픽 라우팅
- 현재 Outbox 테이블: `auth.auth_domain_event`, `user_domain_event`, `creator_domain_event`
- DLT(Dead Letter Topic) 패턴 적용: 실패한 메시지는 `*-dlt` 토픽으로 격리

주요 이벤트 흐름:
```
user-signup       → creator (크리에이터 생성 트리거)
user-banned       → auth (세션 무효화), creator (밴 처리), subscribe (구독 정리)
user-withdrawn    → auth (세션 무효화), creator (탈퇴 처리), subscribe (구독 정리)
creator-revoked   → subscribe (해당 크리에이터 구독 정리)
user-logged-in    → user (세션 시작)
user-logged-out   → user (세션 종료)
```

### 2.4 Inbox Pattern (Kafka Consumer → UseCase 브릿지)

Kafka로 수신한 이벤트를 도메인 서비스에서 처리할 때, 불필요한 추상화 없이 직접 UseCase를 호출하는 경량 패턴을 적용한다.

#### 패턴 구조

```
Kafka Topic
    │
    ▼
┌─────────────────────┐
│ *EventKafkaListener  │  ← Infrastructure Layer (Adapter)
│  - @KafkaListener    │
│  - 이벤트 역직렬화    │
└──────────┬──────────┘
           │ event.userId()
           ▼
┌─────────────────────┐
│ *UseCase            │  ← Application Layer (Port)
│  - void handle*(UserId userId)  │
└──────────┬──────────┘
           │ userId
           ▼
┌─────────────────────┐
│ *Service            │  ← Application Layer (Service)
│  - 도메인 로직 실행   │
└─────────────────────┘
```

#### 적용 예시 (creator 서비스)

**1. Kafka Listener (Infrastructure Adapter)**
```java
@Component
@RequiredArgsConstructor
public class UserEventKafkaListener {
    private final HandleUserWithdrawnUseCase handleUserWithdrawnUseCase;
    private final HandleUserBannedUseCase handleUserBannedUseCase;

    @KafkaListener(topics = "outbox.event.user-withdrawn", groupId = "creator-service")
    public void handleUserWithdrawnEvent(ConsumerRecord<String, UserWithdrawnEvent> record) {
        UserWithdrawnEvent event = record.value();
        handleUserWithdrawnUseCase.handleUserWithdrawn(event.userId());
    }

    @KafkaListener(topics = "outbox.event.user-banned", groupId = "creator-service")
    public void handleUserBannedEvent(ConsumerRecord<String, UserBannedEvent> record) {
        UserBannedEvent event = record.value();
        handleUserBannedUseCase.handleUserBanned(event.userId());
    }
}
```

**2. UseCase Interface (Application Port)**
```java
public interface HandleUserBannedUseCase {
    void handleUserBanned(UserId userId);  // Command 객체 없이 UserId 직접 전달
}

public interface HandleUserWithdrawnUseCase {
    void handleUserWithdrawn(UserId userId);
}
```

**3. UseCase Implementation (Application Service)**
```java
@Service
@RequiredArgsConstructor
public class HandleUserBannedService implements HandleUserBannedUseCase {

    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort eventRepositoryPort;

    @Override
    @Transactional
    public void handleUserBanned(UserId userId) {
        Optional<Creator> creatorOpt = creatorRepositoryPort.findByUserId(userId);
        if (creatorOpt.isEmpty()) return;

        Creator creator = creatorOpt.get();
        if (creator.isBanned()) return;  // 멱등성 보장: 이미 처리된 상태면 Skip

        creator.ban();
        creatorRepositoryPort.update(creator);
        eventRepositoryPort.save(
            CreatorRevokedEvent.builder()
                .creatorId(creator.getId())
                .revokedAt(Instant.now())
                .build()
        );
    }
}
```

#### 설계 원칙

| 원칙 | 설명 |
|------|------|
| **Command 객체 미사용** | Kafka 이벤트 → UseCase 파라미터로 UserId를 직접 전달. `HandleUserBannedCommand` 같은 중복 추상화 방지 |
| **1 인터페이스 1 메서드** | `HandleUserBannedUseCase.handleUserBanned(UserId)`처럼 단일 행위만 정의 |
| **멱등성 보장** | 서비스 계층에서 상태 체크 후 early return. 중복 이벤트 수신해도 부작용 없음 |
| **단순 의존성** | Listener → UseCase → RepositoryPort 3단계로 최소 의존성 유지 |
| **VO 직접 전달** | `UserId` 같은 Value Object를 파라미터로 전달하여 타입 안전성 확보 |

#### Outbox vs Inbox 비교

| 구분 | Outbox (발행) | Inbox (수신) |
|------|---------------|--------------|
| 방향 | 서비스 → Kafka | Kafka → 서비스 |
| 목적 | 도메인 이벤트 영속화 + 비동기 전파 | 외부 이벤트 수신 + 도메인 반응 |
| 구현 | `*DomainEventEntity` 저장 → Debezium CDC → Kafka | Kafka Listener → UseCase 직접 호출 |
| 트랜잭션 | DB 트랜잭션과 동일시 (동일 커밋) | 별도 트랜잭션 (이벤트 처리 단위) |

### 2.4 동기 통신 (gRPC)

실시간 데이터 조회가 필요한 경우 gRPC를 사용한다. 모든 gRPC 클라이언트에 Resilience4j CircuitBreaker가 적용되어 있고, fallback 메서드에서 gRPC Status Code에 따라 예외를 분기한다:
- `NOT_FOUND` → 비즈니스 예외 (4xx, 예: `CreatorNotFoundException`)
- 그 외 → `*ServiceUnavailableException` (503, SystemError)

| Client Service | Server Service | RPC |
|---|---|---|
| auth | user | `LoadUserCredential`, `CheckUserActive` |
| creator | user | `GetUserNickname` |
| creator | subscribe | `GetSubscriberCount`, `IsSubscribed` |
| creator | review | `GetReviewRating` |
| creator | release | `GetReleaseCount`, `GetReleasesByCreatorId` |
| creator | shortform | `GetShortformsByCreatorId` |
| general-goods | creator | `GetCreatorIdByUserId` |
| subscribe | creator | `IsCreatorAndSubscriberSamePerson` |

---

## 3. 마이크로서비스 상세

### 3.1 user (사용자)

핵심 도메인. OAuth2 로그인으로 생성된 사용자의 프로필, 세션, 상태를 관리한다.

- **Aggregate**: `User` (UserId, Nickname, email, UserStatus, UserRole, OAuth2 정보, 세션 상태, 총 사용 시간)
- **Sub-Aggregate**: `Device`, `UserDevice` (멀티디바이스 관리)
- **Enums**: `UserStatus`(ACTIVE, BANNED, DELETED), `UserRole`(USER, CREATOR, ADMIN), `OAuth2Provider`(GOOGLE, NAVER, KAKAO), `DeviceType`
- **Domain Events**: `UserSignupEvent`, `UserBannedEvent`, `UserUnbannedEvent`, `UserWithdrawnEvent`
- **주요 기능**:
  - 프로필 조회/수정
  - 세션 관리 (startSession/endSession, 사용 시간 누적)
  - 관리자: 밴/언밴, 탈퇴 처리
- **특이사항**:
  - gRPC 서버 + 클라이언트 모두 제공 (auth에 credential 제공, review에서 리뷰 조회)
  - Kafka consumer로 auth 서비스의 로그인/로그아웃 이벤트 수신
  - `UserInfoExtractFilter`: Gateway에서 `X-User-Id` 헤더를 추출하여 SecurityContext에 `UserId`로 주입
  - `DeviceDomainService`: 활성 디바이스 수 제한 정책 적용 (최대 1개)
  - 낙관적 락 (`@Version`) + 비관적 락 (`PESSIMISTIC_WRITE`) 병용: ban/unban/withdraw 시 비관적 락
  - `Nickname` VO: 6~20자, 한글/영어/숫자만 허용
  - 커스텀 Validation: `@OptionalNotBlank` (null 허용, 빈 문자열 거부)
  - 총 사용 시간 추적: `startSession`/`endSession` 시 `Duration` 누적

### 3.2 auth (인증)

OAuth2 소셜 로그인, JWT 발급/갱신, 세션 관리를 담당한다.

- **Aggregate**: `Code` (OAuth2 인가 코드 임시 저장), `RefreshToken`
- **Enums**: `UserRole`
- **Domain Events**: `LoginEvent`, `LogoutEvent`, `DuplicateLoginEvent`
- **저장소**: Redis(Code, RefreshToken), MySQL(AuthDomainEvent for Outbox)
- **주요 기능**:
  - OAuth2 소셜 로그인 (Google, Naver, Kakao) - Strategy Pattern 적용
  - JWT Access/Refresh Token 발급 및 갱신
  - 로그아웃
  - 사용자 밴/탈퇴 시 세션 무효화 (Kafka 수신)
- **특이사항**:
  - `OAuth2StrategyComposite`: Provider별 전략 객체를 Composite로 관리 (Strategy Pattern)
  - `CookieManager`: Refresh Token을 HttpOnly Secure Cookie로 관리 (prod: SameSite=Strict)
  - One-Time Code 패턴: OAuth2 성공 시 Redis에 임시 코드 저장 → 프론트에서 코드로 토큰 교환
  - Refresh Token Rotation: 갱신 시 기존 토큰 폐기 + 새 토큰 발급
  - 중복 로그인 감지: `UserStatusPort.isActive()` → `DuplicateLoginEvent` 발행
  - gRPC Client → user 서비스에 credential 조회, 상태 확인
  - Debezium CDC의 소스 테이블(`auth_domain_event`)이 이 서비스에 위치

### 3.3 creator (크리에이터)

마술사(크리에이터) 프로필, 인증 심사, 검색을 관리한다. 가장 큰 서비스(204개 Java 파일).

- **Aggregate**: `Creator` (CreatorId, UserId, Nickname, tagline, genres, CreatorStatus), `CreatorCertification` (인증 심사)
- **Enums**: `CreatorStatus`(ACTIVE, BANNED, DELETED), `CreatorCertificationStatus`(PENDING, APPROVED, REJECTED), `MagicGenre`
- **Domain Events**: `CreatorCertificationApprovedEvent`, `CreatorCertificationRejectedEvent`, `CreatorRevokedEvent`, `CreatorUnbannedEvent`
- **주요 기능**:
  - 크리에이터 프로필 CRUD
  - 크리에이터 인증 심사 (신청 → 관리자 승인/반려)
  - 커서 기반 페이지네이션 검색 (`CursorResponse`)
  - 관리자: 밴/언밴, 탈퇴 처리
- **gRPC 서버**: `CreatorGrpcService` (동일인물 판별), `GeneralGoodsGrpcService` (userId → creatorId 변환)
- **gRPC 클라이언트**: user(닉네임), subscribe(구독자수, 구독여부), review(별점), release(작품수), shortform(숏폼)
- **특이사항**:
  - 프로필 조회 시 여러 서비스에서 데이터를 gRPC로 조합 (BFF 역할)
  - `CreatorCertificationRequest`/`CreatorCertificationResult` VO로 심사 요청/결과 구조화
  - 비관적 락: ban/unban/withdraw 시 `findByNicknameWithLock` 사용
  - 커스텀 Validation: `@CursorSize`
  - 도메인 이벤트 Outbox: `CreatorDomainEventEntity`에 JSON 직렬화하여 저장
  - `Nickname` VO: 2~20자
  - `CreatorPolicyConstants`: tagline 최대 50자, introduction 최대 500자

### 3.4 general-goods (일반 상품)

크리에이터가 판매하는 일반 상품의 CRUD를 담당한다.

- **Aggregate**: `GeneralGoods` (GeneralGoodsId, CreatorId, name, price, stock, description, categories, mediaList)
- **Value Objects**: `GeneralGoodsMedia` (mediaUrl, sortOrder) — 불변 클래스(`@Getter` + `@Builder`), 식별자 없음, 통째 교체 방식으로만 관리
- **Enums**: `MagicGenre`
- **주요 기능**:
  - 상품 등록/수정/삭제 (소유자 검증 포함)
  - 상품 조회/키워드 검색
- **gRPC 클라이언트**: creator (userId → creatorId 변환)
- **특이사항**:
  - Kafka 미사용 (동기 gRPC만 사용)
  - `CreatorIdQueryPort`를 통해 상품 등록/수정/삭제 시 소유자 검증
  - gRPC fallback에서 `StatusRuntimeException`의 Status Code 분기: `NOT_FOUND` → `CreatorNotFoundException`(404, 크리에이터가 아닌 사용자), 그 외 → `CreatorServiceUnavailableException`(503)
  - `CreatorNotFoundException`은 `adapter/out/communication/grpc/exception/`에 위치 (general-goods 도메인 예외가 아닌 인프라 예외)
  - `GeneralGoodsRepositoryPort.findById()`는 adapter에서 `Optional.empty()` 시 `GeneralGoodsNotFoundException`을 던짐 (서비스 레이어가 아닌 인프라에서 예외 처리)
  - `GeneralGoodsMedia`는 VO로 설계: 자체 식별자 없음, `changeXxx` 메서드 없음, `GeneralGoods.update()` 시 리스트 통째 재할당
  - `GeneralGoodsEntity.addMedia(mediaEntity)` 헬퍼로 양방향 연관관계(FK) 설정
  - 환경별 설정 파일: jackson SNAKE_CASE, datasource, JPA, gRPC client(creator), security.trusted.ips, resilience4j CircuitBreaker 설정 포함

### 3.5 subscribe (구독)

사용자-크리에이터 간 구독 관계를 관리한다.

- **Aggregate**: `Subscription` (SubscriptionId, SubscriberId, CreatorId)
- **주요 기능**:
  - 구독/구독취소
  - 구독자 수 조회, 구독 여부 확인
  - 사용자 밴/탈퇴 시 해당 사용자의 구독 전체 삭제
  - 크리에이터 해제 시 해당 크리에이터의 구독 전체 삭제
- **gRPC 서버**: `SubscribeGrpcService` (구독자수, 구독여부)
- **gRPC 클라이언트**: creator (동일인물 판별 - 자기 자신 구독 방지)
- **Kafka Consumer**: `UserEventKafkaListener` (user-banned, user-withdrawn), `CreatorEventKafkaListener` (creator-revoked)
- **특이사항**:
  - 멱등성 보장 (이벤트 리스너에서 중복 처리 방지)
  - `SelfSubscriptionNotAllowedException`: 자기 자신 구독 차단 (gRPC로 creator 서비스에 동일인물 확인)

### 3.6 ~ 3.10 미구현 서비스 (Skeleton)

다음 5개 서비스는 빈 build.gradle과 Application 클래스만 존재하는 스켈레톤 상태이다.

| 서비스 | 예상 도메인 |
|--------|------------|
| **order** | 주문/결제 |
| **release** | 마술 작품 발매 |
| **shopping-cart** | 장바구니 |
| **search** | 통합 검색 |
| **waiting** | 대기열 |

---

## 4. 인프라스트럭처

### 4.1 로컬 개발 환경 (Docker Compose)

`docker-compose.local.yml`로 다음 컨테이너를 구성한다:
- **MySQL 8.0**: binlog ROW 모드 활성화 (Debezium CDC 전제)
- **Redis 7**: AOF 영속화, auth 서비스의 Code/RefreshToken 저장
- **Kafka 3.7**: KRaft 모드 (ZooKeeper 미사용), 단일 노드 broker+controller
- **Debezium Connect 3.1**: MySQL → Kafka CDC 파이프라인
- **Kafka UI**: 토픽 모니터링

### 4.2 개발 서버 (Kubernetes)

`dev-infra/` 아래에 서비스별 K8s 매니페스트가 있다:
- 서비스별 `namespace.yaml`, `deployment.yaml`, `service.yaml`, `secret.yaml`
- 인프라: mysql, redis, kafka, debezium도 별도 K8s 리소스로 관리
- 리소스 할당: 512Mi~1Gi 메모리, 250m~500m CPU
- Health Check: `/actuator/health` 엔드포인트로 liveness/readiness probe
- 이미지: DockerHub `magicbox08/*:latest`

### 4.3 CI/CD (GitHub Actions)

- **PR Validation** (`pull-request-validate.yml`):
  - PR 제목 컨벤션 검사: `<type>(<scope>): <subject>` (feat/fix/refactor/test)
  - PR 설명 필수 섹션 검사: 관련 이슈, 변경 사항 요약, 테스트 체크리스트

- **SonarCloud** (`sonarcloud-analyze.yml`):
  - PR 시 자동 코드 분석
  - 보일러플레이트 코드 중복 검사 제외 (GlobalExceptionHandler, BaseEntity, SecurityConfiguration, UserInfoExtractFilter)

---

## 4.4 Git 워크플로우

### 브랜치 전략

모든 변경은 반드시 **Jira 이슈 → 브랜치 → 커밋 → PR** 순서를 따른다. `main` 직접 push는 절대 금지.

```
main
 └── feat/<jira-issue-number>   예: feat/177
 └── fix/<jira-issue-number>    예: fix/143
 └── refactor/<jira-issue-number>
```

- 브랜치명은 `<type>/<jira-issue-number>` 형식
- 각 서비스는 독립 레포(`MagicBoxLian0408/<service>`)에서 관리
- monorepo(`f-lab-edu/MagicBox`)는 전체 코드베이스 참조용이며, PR은 반드시 **서비스 레포**에 생성

### 커밋 컨벤션

```
<jira-issue-number> :: <변경 내용 요약>
```

예시:
```
feat/177 :: 상품 생성/수정 이벤트 필드 확장 및 Outbox 발행 추가
fix/143 :: ForwardedHeaderFilter 제거로 userId null NPE 수정
refactor/167 :: InboxEvent event_id 제거 및 IdempotentAspect Kafka key 기반으로 변경
```

- 커밋은 작은 논리 단위로 분리 (계층별 / 파일 성격별 / 서비스별)
- push 전 반드시 로컬 빌드 성공 확인: `./gradlew compileJava`

### PR 템플릿

PR 설명에는 반드시 3개 섹션이 포함되어야 한다 (CI 검사 항목):

```markdown
## 관련 이슈
KAN-<number>

## 변경 사항 요약
- 변경 내용 bullet

## 테스트 체크리스트
- [ ] 항목 1
- [ ] 항목 2
```

### PR 제목 컨벤션

```
<type>/<issue-number> :: <요약>
```

- type: `feat`, `fix`, `refactor`, `test`, `docs`

---

## 5. 공통 패턴 및 설계 원칙

### 5.1 예외 계층

```
BaseException (abstract, RuntimeException)
  ├── BusinessException (4xx Client Error)
  │     ├── *NotFoundException (404)
  │     ├── *AlreadyExistsException (409)
  │     ├── *UnauthorizedException (403)
  │     └── InvalidFieldException (400)
  └── SystemError (5xx Server Error)
        └── *ServiceUnavailableException (503, gRPC fallback)
```

- `BusinessException` 생성 시 4xx가 아닌 HttpStatus를 넣으면 `SystemError`를 던지는 방어 로직 (모든 서비스 공통 적용)
- `GlobalExceptionHandler`에서 `ErrorResponse`로 통일된 에러 응답
- `BusinessException extends RuntimeException` 직접 상속 방식은 사용하지 않는다.

### 5.2 보안 체인

모든 서비스에 동일한 보안 필터 체인이 적용된다:
- `UserInfoExtractFilter`: API Gateway에서 헤더로 전달된 userId, userRole을 추출하여 `SecurityContext`에 `UserId`로 저장
- `TrustedIpProperties`는 사용하지 않는다.

### 5.3 Value Object 패턴

모든 식별자는 Record 기반 VO로 래핑한다:
- `UserId(Long value)`, `CreatorId(Long value)`, `GeneralGoodsId(Long value)` 등
- VO 생성 시 유효성 검증 (null, 음수 방지)
- Primitive Obsession 방지, 타입 안전성 확보

### 5.4 Port 네이밍 컨벤션

#### Out Port 인터페이스 네이밍

- **Repository Port**: `xxxRepositoryPort` (DB 저장소 접근)
- **Outbox Port**: `xxxOutboxPort` (Outbox 이벤트 발행)
- 그 외 외부 시스템 접근: `xxxPort`

```java
// 올바른 예시
CreatorRepositoryPort       // DB 저장소
CreatorOutboxPort           // Outbox 발행
UserNicknameQueryPort       // gRPC 등 외부 조회
```

`CreatorOutboxRepositoryPort` 처럼 Outbox에 Repository를 붙이는 방식은 사용하지 않는다.

#### Repository Port 메서드 네이밍

모든 서비스의 `*RepositoryPort`에서 조회 메서드는 `find*` 접두사를 사용한다.

| 패턴 | 설명 |
|------|------|
| `findById` | ID로 단건 조회 |
| `findByUserId` | UserId로 조회, `Optional` 반환 |
| `findByNickname` | 닉네임으로 조회, `Optional` 반환 |
| `findByOauth2IdAndProvider` | OAuth2 식별자로 조회, `Optional` 반환 |

- `find*WithLock` 네이밍 금지: 비관적 락이 필요한 쿼리만 별도 메서드로 분리 (현재 미사용)
- `get*` 접두사는 사용하지 않는다.
- 락 필요 시 JPA `@Version` 기반 낙관적 락 사용 (엔터티 차원)

### 5.5 @Transactional 위치

`@Override` 를 먼저 선언하고 `@Transactional` 을 그 아래에 선언한다.

```java
// 올바른 방식
@Override
@Transactional
public void updateCreatorProfile(UpdateCreatorProfileCommand command) { ... }

// 잘못된 방식
@Transactional
@Override
public void updateCreatorProfile(UpdateCreatorProfileCommand command) { ... }
```

### 5.6 Mapper 패턴

도메인 ↔ 엔티티 변환은 전용 Mapper 클래스가 담당한다:
- `*Mapper.toEntity(Domain)` → 영속 엔티티 변환
- `*Mapper.toDomain(Entity)` → 도메인 객체 변환
- 도메인 계층이 JPA에 의존하지 않도록 격리

### 5.12 Aggregate 생성 패턴

Aggregate의 생성 경로를 두 개의 `@Builder`로 명확히 분리한다. 생성자를 두 개로 나누고 각각 다른 빌더 이름을 부여한다.

**`createBuilder()`** — Service에서 호출. 클라이언트 요청으로 신규 도메인 객체를 생성할 때 사용.
- `id`, `status` 등 내부에서 고정해야 하는 필드는 생성자 파라미터에서 제외한다.
- 도메인 불변식 검증(필수값 등)을 생성자 내부에서 수행한다.

```java
// Service에서 호출
Creator creator = Creator.createBuilder()
        .userId(userId).nickname(nickname).genres(genres)
        .build();  // id=null(Sonyflake 부여), status=ACTIVE 자동 고정
```

**`reconstructBuilder()`** — Mapper의 `toDomain()`에서만 호출. DB 엔티티 → 도메인 재구성.
- `id`, `status` 포함 모든 필드를 파라미터로 받는다.
- 비즈니스 규칙 검증은 하지 않으나, `id`/`status` 등 핵심 필드의 null 방어 검증은 수행한다.

```java
// Mapper에서 호출
return Creator.reconstructBuilder()
        .id(CreatorId.of(entity.getId()))
        .userId(UserId.of(entity.getUserId()))
        .status(entity.getStatus())
        ...
        .build();
```

`builder()` 메서드는 존재하지 않으므로, 어느 쪽 빌더를 써야 하는지 컴파일러가 강제한다.

### 5.7 Sonyflake 분산 ID

ID 생성에 `com.github.lian2945:sonyflake`를 사용한다. Twitter Snowflake의 변형으로, 분산 환경에서 유일한 Long ID를 생성한다.

### 5.8 Kafka 컨벤션

#### Kafka Consumer 파라미터 타입

모든 `@KafkaListener` 메서드는 `ConsumerRecord<String, DomainType>` 을 파라미터로 받는다. `ConsumerRecord<String, String>` + 수동 역직렬화 방식은 사용하지 않는다.

```java
// 올바른 방식
@KafkaListener(topics = "outbox.event.user-withdrawn", groupId = "creator-service")
public void handleUserWithdrawnEvent(ConsumerRecord<String, UserWithdrawnEvent> record) {
    handleUserWithdrawnUseCase.handleUserWithdrawn(record.value().userId());
}
```

#### InboxEvent 인터페이스

Kafka 이벤트 DTO가 구현하는 `InboxEvent` 인터페이스는 `occurredAt()` 필드만 포함한다. `eventId()` 필드는 사용하지 않는다.

```java
public interface InboxEvent {
    Instant occurredAt();
}
```

#### 멱등성 처리

멱등성은 `ConsumerRecord.key()` (= Outbox 엔티티의 PK) 기반으로 처리한다. `InboxEvent.eventId()` 기반 처리는 사용하지 않는다.

#### Outbox 엔티티 필드

모든 서비스의 Outbox 엔티티는 `eventType` + `payload` 두 필드만 포함한다. `key`, `aggregateKey` 등 별도 key 필드는 사용하지 않는다. Kafka message key는 Outbox 엔티티의 PK(Sonyflake)가 자동으로 사용된다.

```java
// 모든 서비스 공통 Outbox 엔티티 구조
private String eventType;
private String payload;
```

### 5.9 환경별 설정 전략

모든 서비스가 3단계 환경 프로필을 사용한다:
- **local**: plaintext gRPC, `ddl-auto: update`, `missing-topics-fatal: false`
- **dev**: plaintext gRPC, `ddl-auto: update`, trusted IP를 환경변수로 관리
- **prod**: TLS gRPC, `ddl-auto: validate`, `missing-topics-fatal: true`, 더 보수적인 Circuit Breaker 설정

환경 변수는 `env/local.env`, `env/dev.env`, `env/prod.env` 외부 파일에서 `spring.config.import`로 로드한다.

### 5.10 Jackson 직렬화

모든 서비스에서 `SNAKE_CASE` 프로퍼티 네이밍 전략을 사용한다.

### 5.11 동시성 제어

- **낙관적 락**: `@Version` 필드로 충돌 감지 (UserEntity, GeneralGoodsEntity, GeneralGoodsMediaEntity, CreatorCertificationEntity)
- **비관적 락**: `PESSIMISTIC_WRITE`로 상태 변경 시 동시 접근 방지 (ban/unban/withdraw)
- `ObjectOptimisticLockingFailureException` → 409 Conflict로 핸들링

---

## 6. 서비스 간 의존성 맵

```
                    ┌─────────────┐
                    │   auth      │
                    └──────┬──────┘
                 gRPC(credential,│status)      Kafka(login/logout)
                           │                          │
                    ┌──────▼──────┐            ┌──────▼──────┐
                    │   user      │◄───────────│   user      │
                    └──────┬──────┘  Kafka     └─────────────┘
                           │ (signup/banned/withdrawn)
              ┌────────────┼────────────┐
              ▼            ▼            ▼
       ┌──────────┐ ┌──────────┐ ┌───────────┐
       │  auth    │ │ creator  │ │ subscribe │
       │(세션무효)│ │(밴/탈퇴) │ │(구독 정리)│
       └──────────┘ └────┬─────┘ └─────┬─────┘
                         │              │
                   gRPC  │         gRPC │
              ┌──────────┘    ┌────────┘
              ▼               ▼
       ┌──────────────────────────┐
       │  creator (gRPC 서버)     │
       │  - 닉네임, 구독, 리뷰    │
       │  - 릴리즈, 숏폼 조합     │
       └────────────┬─────────────┘
               gRPC │ (creatorId 조회)
                    ▼
            ┌──────────────┐
            │ general-goods│
            └──────────────┘
```

---

## 7. 기술 스택 버전 요약

| 기술 | 버전 |
|------|------|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring gRPC | 1.0.2 |
| Spring Cloud | 2025.1.0 |
| gRPC | 1.79.0 |
| Protobuf | 4.34.0 |
| MySQL | 8.0 |
| Redis | 7 |
| Kafka | 3.7.0 (KRaft) |
| Debezium | 3.1 |
| Resilience4j | Spring Cloud 번들 |
| JWT (jjwt) | 0.13.0 |
| Lombok | Spring Boot 관리 |
| SonarCloud | GitHub Actions 통합 |