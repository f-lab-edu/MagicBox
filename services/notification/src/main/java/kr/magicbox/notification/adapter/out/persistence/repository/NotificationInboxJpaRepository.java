package kr.magicbox.notification.adapter.out.persistence.repository;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationInboxJpaRepository extends JpaRepository<NotificationInboxEntity, Long> {
    boolean existsByKey(String key);
    Optional<NotificationInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
