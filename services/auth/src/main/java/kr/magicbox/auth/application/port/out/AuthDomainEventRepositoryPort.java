package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.event.AuthDomainEvent;

public interface AuthDomainEventRepositoryPort {
    void save(AuthDomainEvent event);
}
