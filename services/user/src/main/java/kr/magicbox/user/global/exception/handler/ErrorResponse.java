package kr.magicbox.user.global.exception.handler;

import org.springframework.http.HttpStatus;

public record ErrorResponse(int status, String message) {
    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), message);
    }
}
