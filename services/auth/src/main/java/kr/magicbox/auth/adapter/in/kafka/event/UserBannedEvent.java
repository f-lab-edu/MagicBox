package kr.magicbox.auth.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserBannedEvent(
        @JsonProperty("user_id") Long userId,
        @JsonProperty("occurred_at") @JsonAlias("banned_at") Instant occurredAt
) implements InboxEvent {}
