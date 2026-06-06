package kr.magicbox.payment.application.port.out;

import kr.magicbox.payment.domain.aggregate.Payment;

import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    void update(Payment payment);
    Optional<Payment> findByOrderId(Long orderId);
}
