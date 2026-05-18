package kr.magicbox.settlement.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettlementDomainEventType {
    SETTLEMENT_READY("settlement-ready"),
    SETTLEMENT_SETTLED("settlement-settled"),
    SETTLEMENT_CANCELLED("settlement-cancelled"),
    SETTLEMENT_REVERSAL_NEEDED("settlement-reversal-needed");

    private final String value;
}
