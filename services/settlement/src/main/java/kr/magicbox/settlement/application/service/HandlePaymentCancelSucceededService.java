package kr.magicbox.settlement.application.service;

import kr.magicbox.settlement.application.port.in.HandlePaymentCancelSucceededUseCase;
import kr.magicbox.settlement.application.port.out.SettlementOutboxPort;
import kr.magicbox.settlement.application.port.out.SettlementRepositoryPort;
import kr.magicbox.settlement.domain.aggregate.Settlement;
import kr.magicbox.settlement.domain.enums.SettlementStatus;
import kr.magicbox.settlement.domain.event.SettlementCancelledEvent;
import kr.magicbox.settlement.domain.event.SettlementReversalNeededEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HandlePaymentCancelSucceededService implements HandlePaymentCancelSucceededUseCase {

    private final SettlementRepositoryPort settlementRepositoryPort;
    private final SettlementOutboxPort settlementOutboxPort;

    @Override
    @Transactional
    public void handlePaymentCancelSucceeded(Long orderId) {
        List<Settlement> settlements = settlementRepositoryPort.findByOrderId(orderId);
        if (settlements.isEmpty()) {
            return;
        }

        List<Long> cancelledOrderLineIds = new ArrayList<>();
        List<Long> reversalRequiredOrderLineIds = new ArrayList<>();

        for (Settlement settlement : settlements) {
            if (settlement.getStatus() == SettlementStatus.SETTLED) {
                reversalRequiredOrderLineIds.add(settlement.getOrderLineId());
                continue;
            }
            if (settlement.getStatus() == SettlementStatus.CANCELLED) {
                continue;
            }
            settlement.cancel();
            settlementRepositoryPort.update(settlement);
            cancelledOrderLineIds.add(settlement.getOrderLineId());
        }

        Instant occurredAt = Instant.now();

        if (!cancelledOrderLineIds.isEmpty()) {
            settlementOutboxPort.save(SettlementCancelledEvent.builder()
                    .orderId(orderId)
                    .orderLineIds(cancelledOrderLineIds)
                    .occurredAt(occurredAt)
                    .build());
        }

        if (!reversalRequiredOrderLineIds.isEmpty()) {
            settlementOutboxPort.save(SettlementReversalNeededEvent.builder()
                    .orderId(orderId)
                    .orderLineIds(reversalRequiredOrderLineIds)
                    .occurredAt(occurredAt)
                    .build());
        }
    }
}
