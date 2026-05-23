package kr.magicbox.payment.adapter.out.persistence.repository;

import kr.magicbox.payment.adapter.out.persistence.entity.PaymentOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutboxEntity, Long> {
}
