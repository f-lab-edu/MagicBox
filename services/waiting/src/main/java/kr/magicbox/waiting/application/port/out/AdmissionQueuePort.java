package kr.magicbox.waiting.application.port.out;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import reactor.core.publisher.Mono;

public interface AdmissionQueuePort {

    /** 토큰 발급 시 active 카운트 증가 */
    Mono<Void> activate(ReleaseId releaseId, int count);

    /** 구매 완료 또는 TTL 만료 시 active 카운트 감소 */
    Mono<Void> deactivate(ReleaseId releaseId);

    /** 현재 active 토큰 수 (= queue depth) */
    Mono<Long> getActiveCount(ReleaseId releaseId);

    /** active 카운트 초기화 */
    Mono<Void> clear(ReleaseId releaseId);
}
