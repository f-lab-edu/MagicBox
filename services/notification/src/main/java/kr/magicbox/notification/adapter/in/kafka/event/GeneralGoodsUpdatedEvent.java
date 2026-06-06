package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GeneralGoodsUpdatedEvent(
        @JsonProperty("general_goods_id") Long generalGoodsId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
