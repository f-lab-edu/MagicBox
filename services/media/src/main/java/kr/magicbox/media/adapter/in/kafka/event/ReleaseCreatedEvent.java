package kr.magicbox.media.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record ReleaseCreatedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("media_urls") List<String> mediaUrls,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {}
