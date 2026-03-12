package kr.magicbox.user.global.exception.handler;

import kr.magicbox.user.global.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException() {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException() {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 파라미터입니다.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage() : "인자값이 유효하지 않습니다.";
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(ErrorResponse.of(status, e.getMessage()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        HttpStatus status = e.getStatus();
        log.error(e.getMessage());
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.of(status, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error(e.getMessage());
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.of(status, "알 수 없는 오류가 발생했습니다."));
    }
}
