package kr.magicbox.search.adapter.in.web.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kr.magicbox.search.global.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException e) {
        log.error("요청 본문을 읽을 수 없습니다: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("유효하지 않은 요청 파라미터: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, "유효하지 않은 요청 파라미터입니다."));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        String errorMessage = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage() : "인자값이 유효하지 않습니다.";
        log.error("요청 데이터 유효성 검증 실패: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().isEmpty() ?
                "유효성 검증에 실패했습니다." :
                e.getConstraintViolations().iterator().next().getMessage();
        log.error("유효성 검증 실패: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException e) {
        log.error("지원되지 않는 HTTP 메서드: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED, "지원되지 않는 HTTP 메서드입니다."));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.of(e.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상하지 못한 오류 발생: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."));
    }
}
