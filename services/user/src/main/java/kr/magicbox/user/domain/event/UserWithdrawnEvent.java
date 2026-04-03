package kr.magicbox.user.domain.event;

import kr.magicbox.user.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserWithdrawnEvent(
        UserId userId,
        Instant withdrawnAt
) implements UserDomainEvent {

    @Override
    public String key() {
        return userId.value().toString();
    }

    @Override
    public UserDomainEventType eventType() {
        return UserDomainEventType.USER_WITHDRAWN;
    }
}