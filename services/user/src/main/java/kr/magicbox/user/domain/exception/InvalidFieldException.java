package kr.magicbox.user.domain.exception;

import kr.magicbox.user.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BusinessException {

    public InvalidFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
