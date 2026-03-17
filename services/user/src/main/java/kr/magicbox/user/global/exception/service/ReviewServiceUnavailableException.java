package kr.magicbox.user.global.exception.service;

import kr.magicbox.user.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class ReviewServiceUnavailableException extends SystemError {
    public ReviewServiceUnavailableException(Long userId, Throwable cause) {
        super("리뷰 서비스 호출을 할 수 없습니다: userId=" + userId, HttpStatus.SERVICE_UNAVAILABLE);
        if (cause != null) {
            initCause(cause);
        }
    }
}
