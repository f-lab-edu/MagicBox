package kr.magicbox.auth.domain.event;

public interface AuthDomainEvent {
    String key();
    AuthDomainEventType eventType();
}