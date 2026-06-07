package kr.magicbox.payment.domain.event;

public interface PaymentDomainEvent {
    String key();
    PaymentDomainEventType eventType();
}
