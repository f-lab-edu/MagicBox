package kr.magicbox.waiting.application.port.in;

import kr.magicbox.waiting.application.dto.WaitingStatusResult;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface GetWaitingStatusUseCase {
    Mono<WaitingStatusResult> getStatus(ReleaseId releaseId, UserId userId);
}
