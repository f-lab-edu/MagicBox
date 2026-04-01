package kr.magicbox.auth.domain.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InActiveUserException extends BusinessException {
    public InActiveUserException() {
        super("비활성화된 사용자입니다.", HttpStatus.FORBIDDEN);
    }
}
