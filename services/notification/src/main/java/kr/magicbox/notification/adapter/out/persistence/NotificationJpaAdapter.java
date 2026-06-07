package kr.magicbox.notification.adapter.out.persistence;

import kr.magicbox.notification.adapter.out.persistence.entity.NotificationEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationJpaRepository;
import kr.magicbox.notification.application.port.out.NotificationRepositoryPort;
import kr.magicbox.notification.domain.aggregate.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationJpaAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(NotificationEntity.from(notification));
    }

    @Override
    public List<Notification> findAllByUserId(Long userId) {
        return notificationJpaRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationEntity::toDomain)
                .toList();
    }
}
