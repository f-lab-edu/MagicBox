package kr.magicbox.auth.domain.event;

import kr.magicbox.auth.domain.exception.InvalidFieldException;
import lombok.Builder;

public record AuthDomainEvent(
    AuthDomainEventType eventType,
    String key,
    Object payload
) {
    @Builder
    public AuthDomainEvent {
        if (eventType == null) {
            throw new InvalidFieldException("eventType은 필수입니다");
        }
        if (key == null || key.isBlank()) {
            throw new InvalidFieldException("key는 필수입니다");
        }
        if (payload == null) {
            throw new InvalidFieldException("payload는 필수입니다");
        }
    }
}
