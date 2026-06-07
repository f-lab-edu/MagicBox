package kr.magicbox.notification.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationTemplateEntity;
import kr.magicbox.notification.domain.aggregate.Notification;
import kr.magicbox.notification.domain.enums.NotificationStatus;
import kr.magicbox.notification.domain.enums.NotificationType;

public record NotificationResponse(
        @JsonProperty("notification_id") Long notificationId,
        @JsonProperty("type") NotificationType type,
        @JsonProperty("title") String title,
        @JsonProperty("body") String body,
        @JsonProperty("status") NotificationStatus status
) {
    public static NotificationResponse of(Notification notification, NotificationTemplateEntity template) {
        return new NotificationResponse(
                notification.getId().value(),
                notification.getType(),
                template.getTitle(),
                template.getBody(),
                notification.getStatus()
        );
    }
}
