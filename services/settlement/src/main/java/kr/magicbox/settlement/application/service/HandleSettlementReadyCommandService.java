package kr.magicbox.settlement.application.service;

import kr.magicbox.settlement.application.port.in.HandleSettlementReadyCommandUseCase;
import kr.magicbox.settlement.application.port.out.SettlementOutboxPort;
import kr.magicbox.settlement.application.port.out.SettlementRepositoryPort;
import kr.magicbox.settlement.domain.aggregate.Settlement;
import kr.magicbox.settlement.domain.enums.SettlementStatus;
import kr.magicbox.settlement.domain.event.SettlementReadyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HandleSettlementReadyCommandService implements HandleSettlementReadyCommandUseCase {

    private final SettlementRepositoryPort settlementRepositoryPort;
    private final SettlementOutboxPort settlementOutboxPort;

    @Override
    @Transactional
    public void handleSettlementReadyCommand(Long orderId, Long orderLineId) {
        Settlement settlement = settlementRepositoryPort.findByOrderLineId(orderLineId)
                .orElseGet(() -> settlementRepositoryPort.save(Settlement.createBuilder()
                        .orderId(orderId)
                        .orderLineId(orderLineId)
                        .creatorId(null)
                        .grossAmount(0L)
                        .fee(0L)
                        .creatorAccount(null)
                        .build()));

        if (settlement.getStatus() == SettlementStatus.PENDING) {
            settlement.readyToSettle();
            settlementRepositoryPort.update(settlement);
            settlementOutboxPort.save(SettlementReadyEvent.builder()
                    .settlementId(settlement.getId().value())
                    .orderId(settlement.getOrderId())
                    .orderLineId(settlement.getOrderLineId())
                    .occurredAt(Instant.now())
                    .build());
        }
    }
}
