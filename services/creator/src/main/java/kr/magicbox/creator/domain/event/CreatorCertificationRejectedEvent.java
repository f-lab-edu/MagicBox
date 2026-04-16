package kr.magicbox.creator.domain.event;

import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CreatorCertificationRejectedEvent(
        UserId userId,
        CreatorCertificationId certificationId,
        String reviewMessage,
        Instant reviewedAt
) implements CreatorDomainEvent {

    @Override
    public String key() {
        return userId.value().toString();
    }

    @Override
    public CreatorDomainEventType eventType() {
        return CreatorDomainEventType.CREATOR_CERTIFICATION_REJECTED;
    }
}