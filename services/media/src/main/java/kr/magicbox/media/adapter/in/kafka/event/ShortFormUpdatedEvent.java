package kr.magicbox.media.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record ShortFormUpdatedEvent(
        @JsonProperty("shortform_id") Long shortFormId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("before") ShortFormSnapshot before,
        @JsonProperty("after") ShortFormSnapshot after,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {

    public record ShortFormSnapshot(
            @JsonProperty("title") String title,
            @JsonProperty("media_urls") List<String> mediaUrls
    ) {}
}
