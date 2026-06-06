package kr.magicbox.delivery.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DeliveryStartedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("delivery_id") Long deliveryId,
        @JsonProperty("carrier_code") String carrierCode,
        @JsonProperty("tracking_number") String trackingNumber,
        @JsonProperty("dispatched_at") Instant dispatchedAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements DeliveryDomainEvent {

    @Override
    public String key() {
        return orderLineId.toString();
    }

    @Override
    public DeliveryDomainEventType eventType() {
        return DeliveryDomainEventType.DELIVERY_STARTED;
    }
}
