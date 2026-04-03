package kr.magicbox.user.application.port.out;

import kr.magicbox.user.domain.event.UserDomainEvent;

public interface UserDomainEventRepositoryPort {
    void save(UserDomainEvent event);
}