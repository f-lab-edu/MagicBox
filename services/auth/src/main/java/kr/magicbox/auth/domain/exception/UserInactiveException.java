package kr.magicbox.auth.domain.exception;

import kr.magicbox.auth.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserInactiveException extends BaseException {
    public UserInactiveException() {
        super("비활성 사용자입니다.", HttpStatus.FORBIDDEN);
    }
}
