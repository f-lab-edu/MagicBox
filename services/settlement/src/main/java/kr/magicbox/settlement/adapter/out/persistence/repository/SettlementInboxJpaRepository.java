package kr.magicbox.settlement.adapter.out.persistence.repository;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementInboxJpaRepository extends JpaRepository<SettlementInboxEntity, Long> {
    boolean existsByEventId(Long eventId);
}
