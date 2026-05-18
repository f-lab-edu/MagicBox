package kr.magicbox.waiting.application.port.in;

import kr.magicbox.waiting.application.dto.EnqueueResult;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface EnqueueUseCase {
    Mono<EnqueueResult> enqueue(ReleaseId releaseId, UserId userId);
}
