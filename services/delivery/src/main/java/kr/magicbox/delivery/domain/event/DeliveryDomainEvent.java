package kr.magicbox.delivery.domain.event;

public interface DeliveryDomainEvent {
    String key();

    DeliveryDomainEventType eventType();
}
