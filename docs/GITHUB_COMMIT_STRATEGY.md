# GitHub Commit Strategy

## Format

```
[브랜치명] :: <커밋 메세지>
```

## Rules

- 브랜치명은 커밋 메세지 앞에 prefix로 붙인다.
- `::` 를 구분자로 사용한다.
- 커밋 메세지는 한국어로 작성한다.
- 커밋 메세지는 컴팩트하게, 이 커밋에서 **무엇을 추가/수정했는지** 명확하게 기술한다.
- 불필요한 설명이나 배경 정보는 포함하지 않는다.

## 커밋 단위 분리 원칙

커밋은 **가능한 한 작은 단위**로 나눈다. 하나의 커밋이 하나의 논리적 변경만 담도록 한다.

### 분리 기준

| 기준 | 설명 |
|------|------|
| **계층별 분리** | 도메인, 애플리케이션, 어댑터 계층 변경은 가능하면 별도 커밋 |
| **파일 성격별 분리** | 엔티티/VO, UseCase/Port, Service, Controller, Mapper 등 성격이 다른 파일은 묶지 않음 |
| **서비스별 분리** | 여러 마이크로서비스에 걸친 변경은 서비스 단위로 커밋 분리 |
| **기능별 분리** | 기능 추가와 리팩터링이 섞인 경우 반드시 커밋 분리 |

### 잘못된 예 (하나의 커밋에 너무 많은 변경)
```
feat/49 :: 크리에이터 gRPC 추가 및 예외 처리 및 매퍼 수정
```

### 올바른 예 (단위별로 분리)
```
feat/49 :: GetCreatorIdByUserId UseCase 및 Port 추가
feat/49 :: GetCreatorIdByUserIdService 구현
feat/49 :: GeneralGoods용 gRPC 서버 어댑터 추가
feat/49 :: general_goods.proto 정의 추가
```

## Examples

```
feat/49 :: SonarCloud 경고 제거 어노테이션 설정
feat/49 :: Kafka 이벤트 리스너 멱등성 설정
feat/47 :: subscribe 크리에이터 해제 이벤트 정리 처리 추가
refactor/98 :: user 상태 예외 정리와 auth 이벤트 처리 전 상태 확인 추가
```

---

## 다른 브랜치에 커밋이 필요한 경우 — Stash + Cherry-pick 워크플로

현재 작업 중인 브랜치(A)에 미커밋 변경사항이 있는 상태에서, 다른 브랜치(B)에 특정 파일만 커밋·푸시해야 할 때 사용한다.

### 사전 조건 — 커밋 범위 확인

- 사용자가 범위를 명시한 경우: 해당 파일/디렉터리만 대상으로 한다.
- 사용자가 범위를 명시하지 않은 경우: **반드시 먼저 범위를 지정해달라고 요청한다.** 임의로 범위를 추정하여 진행하지 않는다.

### 절차

```
1. [범위 확인] 사용자로부터 커밋할 파일/디렉터리 범위를 확인한다.

2. [untracked 파일 전체 stage] stash 전에 untracked 파일을 모두 git add한다.
   git add {현재 브랜치(A)의 모든 untracked 파일 경로 ...}
   ※ 이유: stash pop 시 untracked 파일은 복원되지 않아 파일이 유실된다.
     stash 전에 전부 git add하여 tracked(staged) 상태로 만들어야 안전하게 복원된다.

3. [전체 stash] 모든 변경사항(staged 포함)을 stash에 저장한다.
   git stash push -m "stash: {작업 설명}"
   ※ untracked 파일은 step 2에서 이미 staged 처리했으므로 -u 옵션 불필요.
   ※ 이 시점에 working tree는 완전히 깨끗한 상태가 된다.

4. [브랜치(B) 전환] 커밋 대상 브랜치(B)로 전환한다.
   git checkout {B}

5. [cherry-pick으로 파일 복원] stash에서 커밋할 파일만 선택적으로 꺼낸다.
   git checkout stash -- {커밋 대상 파일 경로 ...}
   ※ stash 전체를 pop하지 않고 필요한 파일만 working tree로 가져온다.

6. [커밋] 커밋 규칙에 따라 커밋한다.
   git add {커밋 대상 파일 경로 ...}
   git commit -m "{B} :: {커밋 메세지}"

7. [push] 원격으로 push한다.
   git push origin {B}

8. [복귀] 원래 브랜치(A)로 돌아온다.
   git checkout {A}

9. [stash 복원] stash를 복원하여 원래 작업 상태로 되돌린다.
   git stash pop

10. [stash 커밋 로그 정리] stash pop 완료 후 stash가 남긴 커밋 로그를 제거한다.
    git reflog expire --expire=now --expire-unreachable=now
    git gc --prune=now
    ※ stash는 내부적으로 커밋 오브젝트를 생성한다. 복원 후 이를 정리하여
      git log / reflog를 깔끔하게 유지한다.
```

### 규칙
- **stash 전에 untracked 파일을 반드시 전부 git add한다.** stash pop 시 untracked 파일은 복원되지 않으므로 파일 유실을 막기 위해 필수다.
- 브랜치(B)에 필요한 파일은 stash pop이 아닌 `git checkout stash -- {파일}` 로 선택 복원한다.
- stash 메시지는 `-m` 옵션으로 반드시 명시하여 나중에 식별 가능하게 한다.
- stash pop 후 reflog와 gc로 stash 커밋 기록을 정리한다.
- stash pop 후 충돌이 발생하면 수동으로 해결한다.