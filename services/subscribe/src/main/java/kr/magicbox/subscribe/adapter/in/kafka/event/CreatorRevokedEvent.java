package kr.magicbox.subscribe.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CreatorRevokedEvent(
        @JsonProperty("creator_id") CreatorId creatorId,
        @JsonProperty("revoked_at") Instant revokedAt
) {
}
