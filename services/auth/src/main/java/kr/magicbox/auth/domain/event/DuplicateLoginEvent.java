package kr.magicbox.auth.domain.event;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DuplicateLoginEvent(UserId userId, Instant occurredAt) implements AuthDomainEvent {

    @Override
    public AuthDomainEventType eventType() {
        return AuthDomainEventType.USER_DUPLICATE_LOGGED_IN;
    }
}
