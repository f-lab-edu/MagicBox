package kr.magicbox.delivery.adapter.out.persistence;

import tools.jackson.databind.ObjectMapper;
import kr.magicbox.delivery.adapter.out.persistence.entity.DeliveryOutboxEntity;
import kr.magicbox.delivery.adapter.out.persistence.repository.DeliveryOutboxJpaRepository;
import kr.magicbox.delivery.application.port.out.DeliveryOutboxPort;
import kr.magicbox.delivery.domain.event.DeliveryDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryOutboxAdapter implements DeliveryOutboxPort {

    private final DeliveryOutboxJpaRepository deliveryOutboxJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(DeliveryDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        deliveryOutboxJpaRepository.save(DeliveryOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .build());
    }
}
