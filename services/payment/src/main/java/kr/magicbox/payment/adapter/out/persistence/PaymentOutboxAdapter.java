package kr.magicbox.payment.adapter.out.persistence;

import tools.jackson.databind.ObjectMapper;
import kr.magicbox.payment.adapter.out.persistence.entity.PaymentOutboxEntity;
import kr.magicbox.payment.adapter.out.persistence.repository.PaymentOutboxJpaRepository;
import kr.magicbox.payment.application.port.out.PaymentOutboxPort;
import kr.magicbox.payment.domain.event.PaymentDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxAdapter implements PaymentOutboxPort {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void save(PaymentDomainEvent event) {
        String payload = objectMapper.writeValueAsString(event);
        paymentOutboxJpaRepository.save(PaymentOutboxEntity.builder()
                .eventType(event.eventType().getValue())
                .payload(payload)
                .build());
    }
}
