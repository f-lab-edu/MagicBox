package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UserUnbannedEvent(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
