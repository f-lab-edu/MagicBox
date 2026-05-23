package kr.magicbox.payment.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record PaymentApproveCommandEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("total_amount") long totalAmount,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
