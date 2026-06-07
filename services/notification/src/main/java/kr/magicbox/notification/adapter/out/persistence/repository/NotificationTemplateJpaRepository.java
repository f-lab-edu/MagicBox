package kr.magicbox.notification.adapter.out.persistence.repository;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationTemplateEntity;
import kr.magicbox.notification.domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateEntity, Long> {
    Optional<NotificationTemplateEntity> findByType(NotificationType type);
}
