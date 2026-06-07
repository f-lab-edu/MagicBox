package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record OrderAutoConfirmedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
