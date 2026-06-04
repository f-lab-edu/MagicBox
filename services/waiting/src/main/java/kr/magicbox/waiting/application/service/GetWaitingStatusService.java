package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.application.dto.WaitingStatusResult;
import kr.magicbox.waiting.application.port.in.GetWaitingStatusUseCase;
import kr.magicbox.waiting.application.port.out.PurchaseTokenPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.exception.NotInQueueException;
import kr.magicbox.waiting.global.properties.WaitingProperties;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetWaitingStatusService implements GetWaitingStatusUseCase {

    private final WaitingQueuePort waitingQueuePort;
    private final PurchaseTokenPort purchaseTokenPort;
    private final WaitingProperties waitingProperties;

    @Override
    public Mono<WaitingStatusResult> getStatus(ReleaseId releaseId, UserId userId) {
        return purchaseTokenPort.get(releaseId, userId)
                .map(token -> WaitingStatusResult.builder()
                        .rank(0)
                        .queueSize(0)
                        .estimatedWaitSeconds(0)
                        .purchaseToken(token)
                        .build())
                .switchIfEmpty(waitingQueuePort.getRank(releaseId, userId)
                        .switchIfEmpty(Mono.error(new NotInQueueException()))
                        .zipWith(waitingQueuePort.getQueueSize(releaseId))
                        .map(tuple -> {
                            long rank = tuple.getT1() + 1;
                            long queueSize = tuple.getT2();
                            long estimatedWait = (rank - 1) * waitingProperties.getAvgPurchaseSeconds();
                            return WaitingStatusResult.builder()
                                    .rank(rank)
                                    .queueSize(queueSize)
                                    .estimatedWaitSeconds(estimatedWait)
                                    .build();
                        }));
    }
}
