package kr.magicbox.auth.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;

import java.time.Instant;

@Builder
public record LoginEvent(
        @JsonProperty("user_id") UserId userId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements AuthDomainEvent {

    @Override
    public AuthDomainEventType eventType() {
        return AuthDomainEventType.USER_LOGGED_IN;
    }
}