package kr.magicbox.delivery.application.port.out;

import kr.magicbox.delivery.domain.event.DeliveryDomainEvent;

public interface DeliveryOutboxPort {
    void save(DeliveryDomainEvent event);
}
