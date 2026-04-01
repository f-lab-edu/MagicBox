package kr.magicbox.auth.adapter.out.persistence;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthDomainEventEntity;
import kr.magicbox.auth.adapter.out.persistence.repository.AuthDomainEventRepository;
import kr.magicbox.auth.application.port.out.AuthDomainEventRepositoryPort;
import kr.magicbox.auth.domain.event.AuthDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Repository
@RequiredArgsConstructor
public class AuthDomainEventAdapter implements AuthDomainEventRepositoryPort {

    private final AuthDomainEventRepository authDomainEventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(AuthDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        authDomainEventRepository.save(AuthDomainEventEntity.builder()
                .eventType(event.eventType().getValue())
                .key(event.key())
                .payload(payload)
                .build());
    }
}
