package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CreatorCertificationRejectedEvent(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("certification_id") Long certificationId,
        @JsonProperty("review_message") String reviewMessage,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
