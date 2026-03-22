package kr.magicbox.user.adapter.out.persistence.exception;

import kr.magicbox.user.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}