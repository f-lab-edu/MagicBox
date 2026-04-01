package kr.magicbox.user.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.user.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LogoutEvent(
        @JsonProperty("user_id") UserId userId,
        @JsonProperty("created_at") Instant createdAt
) {}