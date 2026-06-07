package kr.magicbox.settlement.adapter.out.persistence.repository;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementOutboxJpaRepository extends JpaRepository<SettlementOutboxEntity, Long> {
}
