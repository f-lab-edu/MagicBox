package kr.magicbox.user.domain.exception;

import kr.magicbox.user.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserSessionNotActiveException extends BaseException {
    public UserSessionNotActiveException() {
        super("이미 비활성 세션 상태인 사용자입니다.", HttpStatus.CONFLICT);
    }
}
