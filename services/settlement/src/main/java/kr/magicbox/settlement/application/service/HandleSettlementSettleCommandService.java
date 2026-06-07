package kr.magicbox.settlement.application.service;

import kr.magicbox.settlement.application.port.in.HandleSettlementSettleCommandUseCase;
import kr.magicbox.settlement.application.port.out.SettlementOutboxPort;
import kr.magicbox.settlement.application.port.out.SettlementRepositoryPort;
import kr.magicbox.settlement.domain.aggregate.Settlement;
import kr.magicbox.settlement.domain.enums.SettlementStatus;
import kr.magicbox.settlement.domain.event.SettlementSettledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HandleSettlementSettleCommandService implements HandleSettlementSettleCommandUseCase {

    private static final double FEE_RATE = 0.10;

    private final SettlementRepositoryPort settlementRepositoryPort;
    private final SettlementOutboxPort settlementOutboxPort;

    @Override
    @Transactional
    public void handleSettlementSettleCommand(Long orderId, Long orderLineId, Long sellerId, long grossAmount) {
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
        }

        if (settlement.getStatus() != SettlementStatus.READY_TO_SETTLE) {
            return;
        }

        long fee = Math.round(grossAmount * FEE_RATE);
        settlement.applyFinancials(sellerId, grossAmount, fee, null);
        settlement.settle();
        settlementRepositoryPort.update(settlement);

        settlementOutboxPort.save(SettlementSettledEvent.builder()
                .orderId(orderId)
                .orderLineId(orderLineId)
                .netAmount(settlement.getNetAmount())
                .occurredAt(Instant.now())
                .build());
    }
}
