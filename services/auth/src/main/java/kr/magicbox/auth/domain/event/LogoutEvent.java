package kr.magicbox.auth.domain.event;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LogoutEvent(UserId userId, Instant occurredAt) implements AuthDomainEvent {

    @Override
    public AuthDomainEventType eventType() {
        return AuthDomainEventType.USER_LOGGED_OUT;
    }
}
