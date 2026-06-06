package kr.magicbox.payment.adapter.out.persistence;

import kr.magicbox.payment.adapter.out.persistence.entity.PaymentEntity;
import kr.magicbox.payment.adapter.out.persistence.mapper.PaymentMapper;
import kr.magicbox.payment.adapter.out.persistence.repository.PaymentJpaRepository;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.domain.aggregate.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentJpaAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository paymentJpaRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        payment.getPaymentLines().forEach(line ->
                entity.getPaymentLines().add(paymentMapper.toLineEntity(line, entity))
        );
        PaymentEntity saved = paymentJpaRepository.save(entity);
        return paymentMapper.toDomain(saved);
    }

    @Override
    public void update(Payment payment) {
        PaymentEntity entity = paymentJpaRepository.findByOrderId(payment.getOrderId())
                .orElseThrow(() -> new IllegalStateException("결제 엔티티를 찾을 수 없습니다. orderId=" + payment.getOrderId()));
        entity.update(payment.getStatus(), payment.getPaymentKey(), payment.getPaymentMethod());
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentJpaRepository.findByOrderId(orderId)
                .map(paymentMapper::toDomain);
    }
}
