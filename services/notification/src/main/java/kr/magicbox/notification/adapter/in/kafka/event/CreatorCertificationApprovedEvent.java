package kr.magicbox.notification.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CreatorCertificationApprovedEvent(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("certification_id") Long certificationId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
}
