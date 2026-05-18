package kr.magicbox.settlement.adapter.out.persistence;

import tools.jackson.databind.ObjectMapper;
import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementOutboxEntity;
import kr.magicbox.settlement.adapter.out.persistence.repository.SettlementOutboxJpaRepository;
import kr.magicbox.settlement.application.port.out.SettlementOutboxPort;
import kr.magicbox.settlement.domain.event.SettlementDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementOutboxAdapter implements SettlementOutboxPort {

    private final SettlementOutboxJpaRepository settlementOutboxJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(SettlementDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        settlementOutboxJpaRepository.save(SettlementOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .build());
    }
}
