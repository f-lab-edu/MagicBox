package kr.magicbox.settlement.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record SettlementReadyEvent(
        @JsonProperty("settlement_id") Long settlementId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements SettlementDomainEvent {

    @Override
    public String key() {
        return orderLineId.toString();
    }

    @Override
    public SettlementDomainEventType eventType() {
        return SettlementDomainEventType.SETTLEMENT_READY;
    }
}
