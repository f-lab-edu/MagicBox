package kr.magicbox.payment.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record PaymentSucceededEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("payment_id") Long paymentId,
        @JsonProperty("total_amount") long totalAmount,
        @JsonProperty("approved_at") Instant approvedAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements PaymentDomainEvent {

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public PaymentDomainEventType eventType() {
        return PaymentDomainEventType.PAYMENT_SUCCEEDED;
    }
}
