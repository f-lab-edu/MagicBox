package kr.magicbox.auth.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserWithdrawnEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("user_id") Long userId,
        @JsonProperty("withdrawn_at") Instant withdrawnAt
) implements InboxEvent {}