package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GeneralGoodsCreatedEvent(
        @JsonProperty("general_goods_id") Long generalGoodsId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("name") String name,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
