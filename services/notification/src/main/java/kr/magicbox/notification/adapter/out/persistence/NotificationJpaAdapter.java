package kr.magicbox.notification.adapter.out.persistence;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationJpaRepository;
import kr.magicbox.notification.application.port.out.NotificationRepositoryPort;
import kr.magicbox.notification.domain.aggregate.Notification;
import kr.magicbox.notification.domain.enums.NotificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationJpaAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(NotificationEntity.from(notification));
    }

    @Override
    public void update(Notification notification) {
        notificationJpaRepository.findByIdAndUserId(
                        notification.getId().value(), notification.getUserId().value())
                .ifPresent(NotificationEntity::markRead);
    }

    @Override
    public void updateAllByIdsAndUserId(List<Long> notificationIds, Long userId) {
        notificationJpaRepository.updateStatusByIdsAndUserId(notificationIds, userId, NotificationStatus.READ);
    }

    @Override
    public Optional<Notification> findByIdAndUserId(Long notificationId, Long userId) {
        return notificationJpaRepository.findByIdAndUserId(notificationId, userId)
                .map(NotificationEntity::toDomain);
    }

    @Override
    public List<Notification> findAllByUserId(Long userId) {
        return notificationJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationEntity::toDomain)
                .toList();
    }
}
