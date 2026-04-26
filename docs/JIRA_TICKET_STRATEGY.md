# JIRA 티켓 발급 전략

> 프로젝트: MagicBox (KAN) | 작성 기준: Claude Code AI (12년차 PM 관점)
> cloudId: `magicbox0408.atlassian.net`
> 담당자 account_id: `712020:a9492b23-e7cd-44c2-a881-b03e2d0c4393`

---

## 1. 기본 원칙

Claude Code가 코드 작업을 수행할 때, 작업의 성격을 판단하여 **JIRA MCP Tool(`mcp__atlassian__createJiraIssue`)** 을 통해 자동으로 티켓을 발급한다.

- 모든 티켓의 담당자는 `712020:a9492b23-e7cd-44c2-a881-b03e2d0c4393` (magic box)으로 고정
- 티켓은 독립 이슈로 생성하지 않고, **반드시 적합한 에픽의 하위 업무(작업 타입)로 생성**
- 이슈 타입: `작업` (id: `10003`) — 하위 업무의 표준 타입
- 작업 완료 직후 발급, 발급 후 티켓 번호(KAN-xxx)를 사용자에게 보고

---

## 2. 작업 유형 분류

| 유형 | 정의 | 예시 |
|------|------|------|
| **리팩터링** | 외부 동작 변경 없이 코드 구조·품질을 개선하는 작업 | 빌더 패턴 분리, 예외 계층 정리, 메서드 추출, SonarCloud 경고 제거 |
| **기능 추가/변경** | 새로운 비즈니스 요구사항 구현 또는 기존 동작 변경 | Soft Delete 구현, Kafka 이벤트 리스너 추가, API 엔드포인트 신규 개발 |

---

## 3. 에픽(칸반) 목록 — 실제 JIRA 기준

아래는 KAN 프로젝트에 존재하는 모든 에픽이다. 티켓 발급 시 `parent` 필드에 해당 에픽의 key를 지정한다.

### 3-1. 도메인 기능 에픽

| 에픽 Key | 에픽명 | 해당 작업 예시 |
|----------|--------|----------------|
| **KAN-4** | 인증 & 토큰 관리 | 로그인/로그아웃, OAuth2, JWT, 토큰 갱신, 인가 플로우 |
| **KAN-5** | 유저 프로필 | 유저 엔티티, 프로필 조회/수정, 탈퇴, 밴/언밴 |
| **KAN-6** | 크리에이터 | 크리에이터 등록, 인증 심사, 프로필, 밴/탈퇴 |
| **KAN-7** | 구독 | 구독/취소, 구독자 수 조회, 구독 크리에이터 목록 |
| **KAN-8** | 일반 상품 | 상품 등록/수정/삭제, 목록/상세 조회, Soft Delete |
| **KAN-10** | 장바구니 | 담기, 조회, 수량 변경, 삭제 |
| **KAN-11** | 릴리즈 | 릴리즈 등록/수정/삭제, 상태 자동 전환, 알람 |
| **KAN-12** | 웨이팅 | 웨이팅 번호 발급, 조회, 이탈, 구매 차례 도달 |
| **KAN-13** | 주문 | 주문 생성, 내역 조회, 상세 조회 |
| **KAN-14** | 검색 | 최근 검색어, 일반상품 검색, 릴리즈 검색, 통합 검색 |
| **KAN-15** | 알림 | 알림 발송, 알림 목록, 읽음 처리 |
| **KAN-16** | 리뷰/평점 | 리뷰 작성/수정/삭제, 평점 집계 |
| **KAN-17** | Q&A | 질문/답변 등록, 조회 |
| **KAN-18** | 포인트 | 포인트 적립/사용, 내역 조회 |
| **KAN-19** | 배송 관리 | 배송 상태 추적, 배송지 관리 |
| **KAN-20** | 주문 취소 | 취소 신청, 취소 승인/반려, 환불 처리 |
| **KAN-21** | 숏폼 | 숏폼 업로드/수정/삭제, 조회, 좋아요 |
| **KAN-22** | 신고 | 신고 접수, 신고 처리 |
| **KAN-25** | 정산 | 크리에이터 정산 내역, 정산 처리 |

### 3-2. 인프라 / 운영 에픽

| 에픽 Key | 에픽명 | 해당 작업 예시 |
|----------|--------|----------------|
| **KAN-37** | CI/CD 규칙과 마이크로 서비스 설계 | MSA 통신 규약, 아키텍처 설계, GitHub Actions |
| **KAN-93** | 로컬 인프라 구축 | Docker Compose, Kafka, DB, Gateway 로컬 환경 |
| **KAN-27** | 택배사 API 연동 | 택배사 외부 API 연동, 배송 조회 |
| **KAN-28** | 실제 PG 연동 | 결제 PG 연동, 결제 승인/취소 |
| **KAN-92** | 1차 테스트 | 통합 테스트, E2E 테스트, 시나리오 검증 |

### 3-3. 리팩터링 에픽

| 에픽 Key | 에픽명 | 해당 작업 예시 |
|----------|--------|----------------|
| **KAN-101** | 코드 리펙터링 | 빌더 패턴 분리, 예외 계층 정리, SonarCloud 경고 제거, 패키지 구조 개편 |

### 3-4. 관리자 에픽

| 에픽 Key | 에픽명 | 해당 작업 예시 |
|----------|--------|----------------|
| **KAN-23** | 어드민 대시보드 | 관리자 전용 API, 사용자/크리에이터 관리, 통계 |

---

## 4. 티켓 발급 규칙

### 4-1. 리팩터링 작업
- **에픽**: 항상 `KAN-101` (코드 리펙터링)
- **제목 형식**: `[Refactor] {서비스명} :: {리팩터링 유형 또는 대상 — 간결하게}`
  - 제목은 **어떤 리팩터링인지** 파악할 수 있는 수준으로 짧게 유지 (세부 파일명·변경 내용은 본문에 기재)
- **설명 구성**: 변경 동기(Why) / 변경 범위(What) / 영향 파일 목록 — **본문을 충분히 상세하게** 작성

**예시**
```
제목: [Refactor] Aggregate :: Builder 패턴 분리
설명:
  Why: 단일 @Builder로 생성·재구성 목적이 혼재하여 잘못된 신규 생성을 컴파일러가 방지하지 못함
  What: createBuilder / reconstructBuilder 패턴으로 분리
  대상 Aggregate: Creator, CreatorCertification, User, GeneralGoods, Subscription
  영향 파일: CreatorMapper, UserMapper, GeneralGoodsMapper, SubscriptionMapper, LoginService 등
```

### 4-2. 기능 추가/변경 작업
- **에픽**: 아래 판단 기준으로 가장 적합한 에픽 선택
- **제목 형식**: `[Feat] {서비스명} :: {기능 한 줄 요약}`
- **설명 구성**: 배경(Background) / 요구사항(Requirements) / 구현 범위(Scope) / 영향 서비스

**에픽 선택 판단 기준: "어떤 도메인의 데이터를 주로 변경하는가"**

| 요청 내용 키워드 | 배치 에픽 |
|------------------|-----------|
| 로그인, 로그아웃, OAuth, JWT, 토큰 | KAN-4 |
| 유저, 회원, 탈퇴, 밴, 프로필(user) | KAN-5 |
| 크리에이터, 인증 심사 | KAN-6 |
| 구독, 팔로우 | KAN-7 |
| 일반 상품, GeneralGoods | KAN-8 |
| 장바구니 | KAN-10 |
| 릴리즈 | KAN-11 |
| 웨이팅 | KAN-12 |
| 주문 생성, 주문 내역 | KAN-13 |
| 검색, 키워드 | KAN-14 |
| 알림, 푸시 | KAN-15 |
| 리뷰, 평점 | KAN-16 |
| Q&A | KAN-17 |
| 포인트 | KAN-18 |
| 배송 | KAN-19 |
| 주문 취소, 환불 | KAN-20 |
| 숏폼, 동영상 | KAN-21 |
| 신고 | KAN-22 |
| 어드민, 관리자 | KAN-23 |
| 정산 | KAN-25 |
| 택배사 API | KAN-27 |
| PG, 결제 | KAN-28 |
| 인프라, Kafka, gRPC, Gateway, Docker | KAN-37 또는 KAN-93 |
| 테스트, E2E | KAN-92 |

**예시**
```
제목: [Feat] GeneralGoods :: Soft Delete 및 creator-revoked 이벤트 처리
설명:
  Background: 크리에이터 자격 박탈 시 해당 크리에이터의 상품이 계속 노출되는 운영 이슈
  Requirements:
    1. GeneralGoods에 is_deleted 컬럼 추가, 물리 삭제 → Soft Delete 전환
    2. Kafka outbox.event.creator-revoked 수신 시 해당 크리에이터 상품 일괄 Soft Delete
  Scope: GeneralGoods 도메인, JpaRepository, Kafka Listener, JPQL Bulk UPDATE
  영향 서비스: general-goods-service
```

---

## 5. MCP Tool 호출 방법

```
mcp__atlassian__createJiraIssue(
  cloudId: "magicbox0408.atlassian.net",
  projectKey: "KAN",
  issueType: "작업",          // 이슈 타입 고정
  summary: "[Refactor] ...",  // 제목
  description: "...",         // 설명 (markdown)
  assigneeId: "712020:a9492b23-e7cd-44c2-a881-b03e2d0c4393",  // 담당자 고정
  parentKey: "KAN-101"        // 에픽 key (작업 성격에 따라 결정)
)
```

---

## 6. 발급 흐름

```
작업 요청 수신
    ↓
작업 유형 판단 (리팩터링 vs 기능 추가/변경)
    ↓
[리팩터링]                   [기능 추가/변경]
parentKey = KAN-101          섹션 4-2 판단 기준으로 에픽 Key 결정
    ↓                              ↓
코드 작업 수행
    ↓
작업 완료 후 mcp__atlassian__createJiraIssue 호출
    ↓
담당자: 712020:a9492b23-e7cd-44c2-a881-b03e2d0c4393 고정
    ↓
발급된 티켓 번호(KAN-xxx) 사용자에게 보고
```

---

## 7. 발급 제외 대상

- 단순 오타 수정, 주석 추가
- 질문에 대한 설명/답변 (코드 변경 없음)
- 동일 내용의 티켓이 이미 해당 에픽에 존재하는 경우
- MCP 연결 실패 시: 티켓 초안을 대화창에 출력하고 수동 등록 안내

---

## 8. GitHub Branch 자동 생성

JIRA 티켓 발급 직후, **GitHub MCP Tool(`mcp__github__create_branch`)** 을 통해 해당 티켓에 대응하는 브랜치를 자동으로 생성한다.

### 8-1. 브랜치 명명 규칙

| 작업 유형 | 브랜치 prefix | 예시 |
|-----------|--------------|------|
| 기능 추가/변경 | `feat/` | `feat/102` |
| 리팩터링 | `refactor/` | `refactor/103` |

- 브랜치명 형식: `{prefix}{티켓 번호}` (KAN- 제외, 숫자만)
- 예) 티켓 `KAN-102` → 브랜치 `feat/102`
- 예) 티켓 `KAN-103` → 브랜치 `refactor/103`
- 기본 분기점: `main` 브랜치

### 8-2. MCP Tool 호출 방법

```
mcp__github__create_branch(
  owner: "f-lab-edu",
  repo: "MagicBox",
  branch: "feat/102",      // {prefix}{티켓 번호 숫자}
  from_branch: "main"      // 항상 main 기준
)
```

### 8-3. 브랜치 생성 흐름

```
mcp__atlassian__createJiraIssue 호출 → 티켓 발급 (KAN-xxx)
    ↓
작업 유형에 따라 prefix 결정 (feat/ 또는 refactor/)
    ↓
mcp__github__create_branch 호출
  owner: "f-lab-edu", repo: "MagicBox"
  branch: "{prefix}{xxx}", from_branch: "main"
    ↓
사용자에게 티켓 번호(KAN-xxx) + 브랜치명 함께 보고
```

### 8-4. 보고 형식

```
✅ JIRA 티켓 발급 완료: KAN-102
✅ GitHub 브랜치 생성 완료: feat/102 (from: main)
```

---

## 9. 다중 도메인 변경 시 분리 원칙

하나의 작업 세션에서 여러 도메인이 변경된 경우 도메인별로 **티켓을 분리**하여 발급한다.

예) `creator-revoked` Kafka 이벤트 처리 작업:
- `[Feat] Creator :: creator-revoked 이벤트 발행 추가` → **KAN-6**
- `[Feat] GeneralGoods :: creator-revoked 이벤트 수신 후 Soft Delete` → **KAN-8**