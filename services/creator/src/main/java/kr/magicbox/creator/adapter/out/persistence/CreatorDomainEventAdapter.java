package kr.magicbox.creator.adapter.out.persistence;

import kr.magicbox.creator.adapter.out.persistence.entity.CreatorDomainEventEntity;
import kr.magicbox.creator.adapter.out.persistence.repository.CreatorDomainEventRepository;
import kr.magicbox.creator.application.port.out.CreatorDomainEventRepositoryPort;
import kr.magicbox.creator.domain.event.CreatorDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class CreatorDomainEventAdapter implements CreatorDomainEventRepositoryPort {

    private final CreatorDomainEventRepository creatorDomainEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(CreatorDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        creatorDomainEventRepository.save(CreatorDomainEventEntity.builder()
                .eventType(event.eventType().getValue())
                .key(event.key())
                .payload(payload)
                .build());
    }
}