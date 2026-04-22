package kr.magicbox.auth.adapter.out.persistence.repository;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthInboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthInboxRepository extends JpaRepository<AuthInboxEntity, Long> {

    boolean existsByEventId(Long eventId);

    Optional<AuthInboxEntity> findByTopicAndPartitionAndOffset(String topic, Integer partition, Long offset);
}
