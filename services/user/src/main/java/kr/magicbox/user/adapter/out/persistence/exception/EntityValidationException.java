package kr.magicbox.user.adapter.out.persistence.exception;

import kr.magicbox.user.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class EntityValidationException extends BusinessException {

    public EntityValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
