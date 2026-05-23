package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.application.dto.WaitingStatusResult;
import kr.magicbox.waiting.application.port.in.GetWaitingStatusUseCase;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.constants.WaitingConstants;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetWaitingStatusService implements GetWaitingStatusUseCase {

    private final WaitingQueuePort waitingQueuePort;

    @Override
    public Mono<WaitingStatusResult> getStatus(ReleaseId releaseId, UserId userId) {
        return waitingQueuePort.getRank(releaseId, userId)
                .zipWith(waitingQueuePort.getQueueSize(releaseId))
                .map(tuple -> {
                    long rank = tuple.getT1() + 1;
                    long queueSize = tuple.getT2();
                    long estimatedWait = rank * WaitingConstants.AVG_PURCHASE_SECONDS;
                    return WaitingStatusResult.builder()
                            .rank(rank)
                            .queueSize(queueSize)
                            .estimatedWaitSeconds(estimatedWait)
                            .build();
                });
    }
}
