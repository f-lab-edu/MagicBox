package kr.magicbox.waiting.adapter.in.scheduler;

import kr.magicbox.waiting.adapter.out.redis.AdmissionRedisAdapter;
import kr.magicbox.waiting.application.port.out.AdmissionQueuePort;
import kr.magicbox.waiting.application.port.out.PurchaseTokenEventPublishPort;
import kr.magicbox.waiting.application.port.out.PurchaseTokenPort;
import kr.magicbox.waiting.application.port.out.ReleaseQueryPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * AIMD(Additive Increase / Multiplicative Decrease) 기반 배치 입장 스케줄러
 *
 * Netflix concurrency-limits 알고리즘을 대기열 배치 크기 조정에 적용.
 * 참고: https://netflixtechblog.medium.com/performance-under-load-3e6fa9a60581
 *
 * - Additive Increase : queue depth < α 이면 batchSize + 1
 * - Multiplicative Decrease : queue depth > β 이면 batchSize × 0.9
 * - α = floor(3 × log10(batchSize)), β = floor(6 × log10(batchSize))
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdmissionScheduler {

    private static final double DECREASE_RATIO = 0.9;
    private static final double MAX_BATCH_RATIO = 0.2;
    private static final int INITIAL_BATCH_RATIO_PERCENT = 10;

    private final WaitingQueuePort waitingQueuePort;
    private final AdmissionQueuePort admissionQueuePort;
    private final AdmissionRedisAdapter admissionRedisAdapter;
    private final PurchaseTokenPort purchaseTokenPort;
    private final PurchaseTokenEventPublishPort purchaseTokenEventPublishPort;
    private final ReleaseQueryPort releaseQueryPort;
    private final ActiveReleaseRegistry activeReleaseRegistry;

    @Scheduled(fixedDelay = 10_000)
    public void admit() {
        activeReleaseRegistry.getActiveReleaseIds()
                .forEach(releaseId -> processRelease(releaseId).subscribe());
    }

    private Mono<Void> processRelease(ReleaseId releaseId) {
        return releaseQueryPort.getRemainingQuantity(releaseId)
                .flatMap(remaining -> {
                    if (remaining <= 0) {
                        return clearQueue(releaseId);
                    }
                    return admissionRedisAdapter.getBatchSize(releaseId)
                            .flatMap(savedBatchSize -> {
                                int initialBatchSize = (int) Math.max(1,
                                        remaining * INITIAL_BATCH_RATIO_PERCENT / 100.0);
                                int currentBatchSize = savedBatchSize > 0 ? savedBatchSize : initialBatchSize;
                                return admissionQueuePort.getActiveCount(releaseId)
                                        .flatMap(queueDepth -> {
                                            int nextBatchSize = computeNextBatchSize(
                                                    currentBatchSize, queueDepth, remaining);
                                            return admissionRedisAdapter.saveBatchSize(releaseId, nextBatchSize)
                                                    .then(issueBatch(releaseId, nextBatchSize, remaining));
                                        });
                            });
                })
                .doOnError(e -> log.warn("배치 입장 처리 실패 releaseId={}", releaseId.value(), e))
                .onErrorResume(e -> Mono.empty());
    }

    /**
     * AIMD 알고리즘으로 다음 배치 크기 계산
     *
     * α = floor(3 × log10(batchSize)) — 이 이하이면 여유 있음 → +1
     * β = floor(6 × log10(batchSize)) — 이 초과이면 혼잡 → × 0.9
     *
     * Netflix VegasLimit.java 의 alphaFunc / betaFunc 에서 가져온 휴리스틱.
     * 참고: https://github.com/Netflix/concurrency-limits/blob/main/concurrency-limits-core/src/main/java/com/netflix/concurrency/limits/limit/VegasLimit.java
     */
    private int computeNextBatchSize(int currentBatchSize, long queueDepth, int remaining) {
        int log10Clamped = Math.max(1, (int) Math.log10(Math.max(1, currentBatchSize)));
        int alpha = 3 * log10Clamped;
        int beta = 6 * log10Clamped;

        int next;
        if (queueDepth <= alpha) {
            next = currentBatchSize + 1;
        } else if (queueDepth > beta) {
            next = (int) Math.floor(currentBatchSize * DECREASE_RATIO);
        } else {
            next = currentBatchSize;
        }

        int maxBatchSize = (int) Math.floor(remaining * MAX_BATCH_RATIO);
        return Math.max(1, Math.min(next, maxBatchSize));
    }

    private Mono<Void> issueBatch(ReleaseId releaseId, int batchSize, int remaining) {
        int actualSize = Math.min(batchSize, remaining);
        return waitingQueuePort.peekFront(releaseId, actualSize)
                .collectList()
                .flatMap(users -> {
                    if (users.isEmpty()) return Mono.empty();
                    return issueTokens(releaseId, users)
                            .then(waitingQueuePort.removeFront(releaseId, users.size()))
                            .then(admissionQueuePort.activate(releaseId, users.size()));
                });
    }

    private Mono<Void> issueTokens(ReleaseId releaseId, List<UserId> users) {
        return Mono.when(users.stream()
                .map(userId -> purchaseTokenPort.issue(releaseId, userId)
                        .flatMap(token -> purchaseTokenEventPublishPort.publish(releaseId, userId, token)))
                .toList());
    }

    private Mono<Void> clearQueue(ReleaseId releaseId) {
        log.info("릴리즈 매진 - 대기열 종료 releaseId={}", releaseId.value());
        activeReleaseRegistry.deregister(releaseId);
        return waitingQueuePort.clear(releaseId)
                .then(admissionQueuePort.clear(releaseId));
    }
}
