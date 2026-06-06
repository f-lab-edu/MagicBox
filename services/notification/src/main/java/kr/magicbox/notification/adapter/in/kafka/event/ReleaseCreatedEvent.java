package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record ReleaseCreatedEvent(
        @JsonProperty("release_id") Long releaseId,
        @JsonProperty("creator_id") Long creatorId,
        @JsonProperty("title") String title,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
