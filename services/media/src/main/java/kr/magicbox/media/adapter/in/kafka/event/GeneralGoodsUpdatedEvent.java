package kr.magicbox.media.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GeneralGoodsUpdatedEvent(
        @JsonProperty("general_goods_id") Long generalGoodsId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("before") GoodsSnapshot before,
        @JsonProperty("after") GoodsSnapshot after,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {

    public record GoodsSnapshot(
            @JsonProperty("name") String name,
            @JsonProperty("price") Long price,
            @JsonProperty("stock") Integer stock,
            @JsonProperty("media_urls") List<String> mediaUrls
    ) {}
}
