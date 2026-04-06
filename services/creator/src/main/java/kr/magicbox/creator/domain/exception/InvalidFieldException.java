package kr.magicbox.creator.domain.exception;

import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidFieldException extends BaseException {

    public InvalidFieldException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
