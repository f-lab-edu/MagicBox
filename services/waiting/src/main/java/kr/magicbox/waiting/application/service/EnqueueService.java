package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.adapter.in.scheduler.ActiveReleaseRegistry;
import kr.magicbox.waiting.application.dto.EnqueueResult;
import kr.magicbox.waiting.application.port.in.EnqueueUseCase;
import kr.magicbox.waiting.application.port.out.ReleaseQueryPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.global.properties.WaitingProperties;
import kr.magicbox.waiting.domain.exception.AlreadyInQueueException;
import kr.magicbox.waiting.domain.exception.ReleaseNotOnSaleException;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnqueueService implements EnqueueUseCase {

    private final WaitingQueuePort waitingQueuePort;
    private final ReleaseQueryPort releaseQueryPort;
    private final ActiveReleaseRegistry activeReleaseRegistry;
    private final WaitingProperties waitingProperties;

    @Override
    public Mono<EnqueueResult> enqueue(ReleaseId releaseId, UserId userId) {
        log.info("[ENQUEUE] 대기열 진입 시도 releaseId={} userId={}", releaseId.value(), userId.value());
        return releaseQueryPort.isOnSale(releaseId)
                .flatMap(onSale -> {
                    if (!onSale) {
                        log.warn("[ENQUEUE] 판매 중이 아님 releaseId={}", releaseId.value());
                        return Mono.error(new ReleaseNotOnSaleException());
                    }
                    long score = System.currentTimeMillis();
                    return waitingQueuePort.enqueue(releaseId, userId, score)
                        .doOnNext(added -> {
                            if (added) {
                                log.info("[ENQUEUE] ZSet 진입 성공 → ActiveReleaseRegistry 등록 releaseId={} userId={}", releaseId.value(), userId.value());
                                activeReleaseRegistry.register(releaseId);
                            }
                        });
                })
                .flatMap(added -> {
                    if (!added) {
                        log.warn("[ENQUEUE] 이미 대기열 존재 releaseId={} userId={}", releaseId.value(), userId.value());
                        return Mono.error(new AlreadyInQueueException());
                    }
                    return waitingQueuePort.getRank(releaseId, userId)
                            .zipWith(waitingQueuePort.getQueueSize(releaseId));
                })
                .map(tuple -> {
                    long rank = tuple.getT1() + 1;
                    long queueSize = tuple.getT2();
                    long estimatedWait = (rank - 1) * waitingProperties.getAvgPurchaseSeconds();
                    log.info("[ENQUEUE] 완료 releaseId={} userId={} rank={} queueSize={}", releaseId.value(), userId.value(), rank, queueSize);
                    return EnqueueResult.builder()
                            .rank(rank)
                            .queueSize(queueSize)
                            .estimatedWaitSeconds(estimatedWait)
                            .pollingIntervalSeconds(waitingProperties.getPollingIntervalSeconds())
                            .build();
                });
    }
}
