package kr.magicbox.settlement.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record SettlementCancelledEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_ids") List<Long> orderLineIds,
        @JsonProperty("occurred_at") Instant occurredAt
) implements SettlementDomainEvent {

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public SettlementDomainEventType eventType() {
        return SettlementDomainEventType.SETTLEMENT_CANCELLED;
    }
}
