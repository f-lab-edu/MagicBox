package kr.magicbox.waiting.application.port.in;

import kr.magicbox.waiting.domain.vo.UserId;
import reactor.core.publisher.Mono;

public interface DequeueUseCase {

    Mono<Void> dequeue(UserId userId);
}
