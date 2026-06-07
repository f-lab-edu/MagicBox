package kr.magicbox.payment.adapter.out.persistence.repository;

import kr.magicbox.payment.adapter.out.persistence.entity.PaymentInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInboxJpaRepository extends JpaRepository<PaymentInboxEntity, Long> {
    boolean existsByKey(String key);
}
