package kr.magicbox.delivery.adapter.out.persistence.repository;

import kr.magicbox.delivery.adapter.out.persistence.entity.DeliveryOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOutboxJpaRepository extends JpaRepository<DeliveryOutboxEntity, Long> {
}
