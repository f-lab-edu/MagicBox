package kr.magicbox.creator.application.port.out;

import kr.magicbox.creator.domain.event.CreatorDomainEvent;

public interface CreatorDomainEventRepositoryPort {
    void save(CreatorDomainEvent event);
}