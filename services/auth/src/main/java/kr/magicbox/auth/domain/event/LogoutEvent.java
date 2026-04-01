package kr.magicbox.auth.domain.event;

import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LogoutEvent(UserId userId, Instant createdAt) implements AuthDomainEvent {

    @Override
    public String key() {
        return userId.toString();
    }

    @Override
    public AuthDomainEventType eventType() {
        return AuthDomainEventType.USER_LOGGED_OUT;
    }
}
