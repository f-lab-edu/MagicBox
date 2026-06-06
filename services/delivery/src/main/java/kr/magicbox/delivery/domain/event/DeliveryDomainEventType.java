package kr.magicbox.delivery.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryDomainEventType {
    DELIVERY_STARTED("delivery-started"),
    DELIVERY_COMPLETED("delivery-completed");

    private final String value;
}
