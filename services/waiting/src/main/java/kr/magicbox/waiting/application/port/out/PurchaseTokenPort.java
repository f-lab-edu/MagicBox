package kr.magicbox.waiting.application.port.out;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface PurchaseTokenPort {

    /** purchase_token 발급 및 저장 (TTL 적용) */
    Mono<String> issue(ReleaseId releaseId, UserId userId);

    /** 기 발급된 purchase_token 조회 (발급 여부 확인용) */
    Mono<String> get(ReleaseId releaseId, UserId userId);

    /** purchase_token 검증 및 소비 (1회용) */
    Mono<Boolean> consumeIfValid(ReleaseId releaseId, UserId userId, String token);

    /** purchase_token 즉시 무효화 */
    Mono<Void> invalidate(ReleaseId releaseId, UserId userId);
}
