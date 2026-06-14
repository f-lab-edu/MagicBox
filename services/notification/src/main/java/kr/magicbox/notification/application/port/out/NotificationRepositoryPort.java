package kr.magicbox.notification.application.port.out;

import kr.magicbox.notification.domain.aggregate.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {
    void save(Notification notification);
    void update(Notification notification);
    void updateAllByIdsAndUserId(List<Long> notificationIds, Long userId);
    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);
    List<Notification> findAllByUserId(Long userId);
}
