package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.application.port.in.DequeueUseCase;
import kr.magicbox.waiting.application.port.out.PurchaseTokenPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DequeueService implements DequeueUseCase {

    private final WaitingQueuePort waitingQueuePort;
    private final PurchaseTokenPort purchaseTokenPort;

    @Override
    public Mono<Void> dequeue(UserId userId) {
        return waitingQueuePort.findReleaseIdByUserId(userId)
                .flatMap(releaseId -> waitingQueuePort.dequeue(releaseId, userId)
                        .then(purchaseTokenPort.invalidate(releaseId, userId)))
                .doOnSuccess(v -> log.info("대기열 제거 완료 userId={}", userId.value()))
                .onErrorResume(e -> {
                    log.warn("대기열 제거 실패 userId={}", userId.value(), e);
                    return Mono.empty();
                });
    }
}
