package kr.magicbox.waiting.application.port.out;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import reactor.core.publisher.Mono;

public interface ReleaseQueryPort {

    /** 릴리즈가 현재 ON_SALE 상태인지 확인 */
    Mono<Boolean> isOnSale(ReleaseId releaseId);

    /** 릴리즈 잔여 수량 조회 (limitedQuantity - soldQuantity) */
    Mono<Integer> getRemainingQuantity(ReleaseId releaseId);
}
