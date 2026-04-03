package kr.magicbox.auth.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserWithdrawnEvent(
        @JsonProperty("user_id") UserId userId,
        @JsonProperty("withdrawn_at") Instant withdrawnAt
) {}