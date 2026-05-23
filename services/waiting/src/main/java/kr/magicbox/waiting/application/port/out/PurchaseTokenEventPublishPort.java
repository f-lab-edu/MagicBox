package kr.magicbox.waiting.application.port.out;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface PurchaseTokenEventPublishPort {

    Mono<Void> publish(ReleaseId releaseId, UserId userId, String purchaseToken);
}
