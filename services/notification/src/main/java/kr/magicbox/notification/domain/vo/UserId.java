package kr.magicbox.notification.domain.vo;

public record UserId(Long value) {
    public static UserId of(Long value) {
        return new UserId(value);
    }
}
