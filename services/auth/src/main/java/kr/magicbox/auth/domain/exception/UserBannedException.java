package kr.magicbox.auth.domain.exception;

import kr.magicbox.auth.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserBannedException extends BaseException {
    public UserBannedException() {
        super("정지된 사용자입니다.", HttpStatus.FORBIDDEN);
    }
}
