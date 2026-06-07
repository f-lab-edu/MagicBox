package kr.magicbox.notification.adapter.out.persistence.repository;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
