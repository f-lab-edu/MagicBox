package kr.magicbox.auth.domain.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class InvalidUserRoleException extends BusinessException {
    public InvalidUserRoleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}