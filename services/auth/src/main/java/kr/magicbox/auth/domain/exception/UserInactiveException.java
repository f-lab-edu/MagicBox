package kr.magicbox.auth.domain.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class UserInactiveException extends BusinessException {
    public UserInactiveException() {
        super("비활성 사용자입니다.", HttpStatus.FORBIDDEN);
    }
}
