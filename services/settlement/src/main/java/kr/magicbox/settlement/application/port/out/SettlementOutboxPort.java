package kr.magicbox.settlement.application.port.out;

import kr.magicbox.settlement.domain.event.SettlementDomainEvent;

public interface SettlementOutboxPort {
    void save(SettlementDomainEvent event);
}
