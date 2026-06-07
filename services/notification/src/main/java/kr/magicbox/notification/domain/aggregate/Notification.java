package kr.magicbox.notification.domain.aggregate;

import kr.magicbox.notification.domain.enums.NotificationStatus;
import kr.magicbox.notification.domain.enums.NotificationType;
import kr.magicbox.notification.domain.vo.NotificationId;
import kr.magicbox.notification.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {

    private NotificationId id;
    private UserId userId;
    private NotificationType type;
    private NotificationStatus status;

    public static Notification create(UserId userId, NotificationType type) {
        return Notification.builder()
                .userId(userId)
                .type(type)
                .status(NotificationStatus.UNREAD)
                .build();
    }

    public void markRead() {
        this.status = NotificationStatus.READ;
    }
}
