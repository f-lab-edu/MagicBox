package kr.magicbox.waiting.application.service;

import kr.magicbox.waiting.application.port.in.ValidatePurchaseTokenUseCase;
import kr.magicbox.waiting.application.port.out.AdmissionQueuePort;
import kr.magicbox.waiting.application.port.out.PurchaseTokenPort;
import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ValidatePurchaseTokenService implements ValidatePurchaseTokenUseCase {

    private final PurchaseTokenPort purchaseTokenPort;
    private final WaitingQueuePort waitingQueuePort;
    private final AdmissionQueuePort admissionQueuePort;

    @Override
    public Mono<Boolean> validate(ReleaseId releaseId, UserId userId, String token) {
        return purchaseTokenPort.consumeIfValid(releaseId, userId, token)
                .flatMap(valid -> {
                    if (!valid) return Mono.just(false);
                    return admissionQueuePort.deactivate(releaseId).thenReturn(true);
                });
    }
}
