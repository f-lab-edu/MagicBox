package kr.magicbox.notification.application.dto.command;

import kr.magicbox.notification.domain.enums.NotificationType;

public record SaveNotificationCommand(
        Long userId,
        NotificationType type
) {
    public static SaveNotificationCommand of(Long userId, NotificationType type) {
        return new SaveNotificationCommand(userId, type);
    }
}
