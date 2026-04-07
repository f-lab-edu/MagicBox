package kr.magicbox.creator.domain.event;

import kr.magicbox.creator.domain.vo.CreatorId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CreatorRevokedEvent(
        CreatorId creatorId,
        Instant revokedAt
) implements CreatorDomainEvent {

    @Override
    public String key() {
        return creatorId.value().toString();
    }

    @Override
    public CreatorDomainEventType eventType() {
        return CreatorDomainEventType.CREATOR_REVOKED;
    }
}