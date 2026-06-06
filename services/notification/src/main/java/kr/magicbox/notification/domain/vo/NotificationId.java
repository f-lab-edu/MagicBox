package kr.magicbox.notification.domain.vo;

public record NotificationId(Long value) {
    public static NotificationId of(Long value) {
        return new NotificationId(value);
    }
}
