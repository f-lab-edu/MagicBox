package kr.magicbox.subscribe.adapter.out.persistence.repository;

import kr.magicbox.subscribe.adapter.out.persistence.entity.SubscribeInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscribeInboxRepository extends JpaRepository<SubscribeInboxEntity, Long> {

    boolean existsByEventId(Long eventId);

    Optional<SubscribeInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
