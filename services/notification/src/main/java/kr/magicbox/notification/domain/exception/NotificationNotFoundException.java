package kr.magicbox.notification.domain.exception;

import kr.magicbox.notification.global.exception.BusinessException;

public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super("알림을 찾을 수 없습니다.");
    }
}
