package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BusinessException {

    public InvalidFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
