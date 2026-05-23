package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.adapter.in.scheduler.ActiveReleaseRegistry;
import kr.magicbox.waiting.application.dto.EnqueueResult;
import kr.magicbox.waiting.application.port.in.EnqueueUseCase;
import kr.magicbox.waiting.application.port.out.ReleaseQueryPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.constants.WaitingConstants;
import kr.magicbox.waiting.domain.exception.AlreadyInQueueException;
import kr.magicbox.waiting.domain.exception.ReleaseNotOnSaleException;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EnqueueService implements EnqueueUseCase {

    private final WaitingQueuePort waitingQueuePort;
    private final ReleaseQueryPort releaseQueryPort;
    private final ActiveReleaseRegistry activeReleaseRegistry;

    @Override
    public Mono<EnqueueResult> enqueue(ReleaseId releaseId, UserId userId) {
        return releaseQueryPort.isOnSale(releaseId)
                .flatMap(onSale -> {
                    if (!onSale) return Mono.error(new ReleaseNotOnSaleException());
                    long score = System.currentTimeMillis();
                    return waitingQueuePort.enqueue(releaseId, userId, score)
                        .doOnNext(added -> { if (added) activeReleaseRegistry.register(releaseId); });
                })
                .flatMap(added -> {
                    if (!added) return Mono.error(new AlreadyInQueueException());
                    return waitingQueuePort.getRank(releaseId, userId)
                            .zipWith(waitingQueuePort.getQueueSize(releaseId));
                })
                .map(tuple -> {
                    long rank = tuple.getT1() + 1;
                    long queueSize = tuple.getT2();
                    long estimatedWait = rank * WaitingConstants.AVG_PURCHASE_SECONDS;
                    return EnqueueResult.builder()
                            .rank(rank)
                            .queueSize(queueSize)
                            .estimatedWaitSeconds(estimatedWait)
                            .build();
                });
    }
}
