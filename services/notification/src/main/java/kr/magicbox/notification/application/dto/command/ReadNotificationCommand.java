package kr.magicbox.notification.application.dto.command;

import java.util.List;

public record ReadNotificationCommand(
        List<Long> notificationIds,
        Long userId
) {
    public static ReadNotificationCommand of(List<Long> notificationIds, Long userId) {
        return new ReadNotificationCommand(notificationIds, userId);
    }
}
