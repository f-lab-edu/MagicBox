package kr.magicbox.settlement.adapter.out.persistence.repository;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettlementInboxJpaRepository extends JpaRepository<SettlementInboxEntity, Long> {
    boolean existsByKey(String key);
    Optional<SettlementInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
