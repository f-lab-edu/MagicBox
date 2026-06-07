package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record PaymentSucceededEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("total_amount") Long totalAmount,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
