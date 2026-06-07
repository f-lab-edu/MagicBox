package kr.magicbox.settlement.global.exception;

import org.springframework.http.HttpStatus;

public class SystemError extends BaseException {

    public SystemError(String message, HttpStatus status) {
        super(message, status);
    }
}
