package kr.magicbox.settlement.domain.event;

public interface SettlementDomainEvent {
    String key();
    SettlementDomainEventType eventType();
}
