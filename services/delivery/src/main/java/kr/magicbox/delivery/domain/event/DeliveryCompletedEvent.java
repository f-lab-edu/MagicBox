package kr.magicbox.delivery.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DeliveryCompletedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("delivery_id") Long deliveryId,
        @JsonProperty("tracking_number") String trackingNumber,
        @JsonProperty("delivered_at") Instant deliveredAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements DeliveryDomainEvent {

    @Override
    public String key() {
        return orderLineId.toString();
    }

    @Override
    public DeliveryDomainEventType eventType() {
        return DeliveryDomainEventType.DELIVERY_COMPLETED;
    }
}
