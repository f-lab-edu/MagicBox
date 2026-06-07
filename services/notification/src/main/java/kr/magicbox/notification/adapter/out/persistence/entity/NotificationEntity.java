package kr.magicbox.notification.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.notification.domain.aggregate.Notification;
import kr.magicbox.notification.domain.enums.NotificationStatus;
import kr.magicbox.notification.domain.enums.NotificationType;
import kr.magicbox.notification.domain.vo.NotificationId;
import kr.magicbox.notification.domain.vo.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @Builder
    public NotificationEntity(Long userId, NotificationType type, NotificationStatus status) {
        this.userId = userId;
        this.type = type;
        this.status = status;
    }

    public static NotificationEntity from(Notification notification) {
        return NotificationEntity.builder()
                .userId(notification.getUserId().value())
                .type(notification.getType())
                .status(notification.getStatus())
                .build();
    }

    public Notification toDomain() {
        return Notification.builder()
                .id(NotificationId.of(getId()))
                .userId(UserId.of(userId))
                .type(type)
                .status(status)
                .build();
    }

    public void markRead() {
        this.status = NotificationStatus.READ;
    }
}
