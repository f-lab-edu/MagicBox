package kr.magicbox.creator.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserBannedEvent(
        @JsonProperty("user_id") UserId userId,
        @JsonProperty("banned_at") Instant bannedAt
) {}