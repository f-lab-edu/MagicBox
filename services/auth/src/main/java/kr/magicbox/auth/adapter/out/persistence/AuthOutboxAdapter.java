package kr.magicbox.auth.adapter.out.persistence;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthOutboxEntity;
import kr.magicbox.auth.adapter.out.persistence.repository.AuthOutboxRepository;
import kr.magicbox.auth.application.port.out.AuthOutboxPort;
import kr.magicbox.auth.domain.event.AuthDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AuthOutboxAdapter implements AuthOutboxPort {

    private final AuthOutboxRepository authOutboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(AuthDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        log.info(payload);
        authOutboxRepository.save(AuthOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .build());
    }
}
