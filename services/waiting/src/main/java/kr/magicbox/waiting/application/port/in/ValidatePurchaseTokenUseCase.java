package kr.magicbox.waiting.application.port.in;

import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface ValidatePurchaseTokenUseCase {
    Mono<Boolean> validate(ReleaseId releaseId, UserId userId, String token);
}
