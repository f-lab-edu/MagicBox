package kr.magicbox.user.adapter.out.persistence;

import kr.magicbox.user.adapter.out.persistence.entity.UserDomainEventEntity;
import kr.magicbox.user.adapter.out.persistence.repository.UserDomainEventRepository;
import kr.magicbox.user.application.port.out.UserDomainEventRepositoryPort;
import kr.magicbox.user.domain.event.UserDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class UserDomainEventAdapter implements UserDomainEventRepositoryPort {

    private final UserDomainEventRepository userDomainEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(UserDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        userDomainEventRepository.save(UserDomainEventEntity.builder()
                .eventType(event.eventType().getValue())
                .key(event.key())
                .payload(payload)
                .build());
    }
}