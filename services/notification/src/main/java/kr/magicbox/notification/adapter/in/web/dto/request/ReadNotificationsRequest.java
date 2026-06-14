package kr.magicbox.notification.adapter.in.web.dto.request;

import java.util.List;

public record ReadNotificationsRequest(List<Long> notificationIds) {
}
