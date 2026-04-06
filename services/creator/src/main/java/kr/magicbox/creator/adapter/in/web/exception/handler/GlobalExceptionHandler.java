package kr.magicbox.creator.adapter.in.web.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NoResourceFoundException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            HttpRequestMethodNotSupportedException.class,
            NullPointerException.class,
            ObjectOptimisticLockingFailureException.class
    })
    public ResponseEntity<ErrorResponse> handleCommonException(Exception exception) {
        HttpStatus status = resolveStatus(exception);
        String message = resolveMessage(exception);
        logException(exception, status, message);
        return buildResponse(status, message);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        HttpStatus status = e.getStatus();
        return ResponseEntity
                .status(status)
                .body(ErrorResponse.of(status, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상하지 못한 오류 발생: {}", e.getMessage(), e);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.");
    }

    private HttpStatus resolveStatus(Exception exception) {
        if (exception instanceof NoResourceFoundException) {
            return HttpStatus.NOT_FOUND;
        }
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
        if (exception instanceof ObjectOptimisticLockingFailureException) {
            return HttpStatus.CONFLICT;
        }
        if (exception instanceof NullPointerException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.BAD_REQUEST;
    }

    private String resolveMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            return methodArgumentNotValidException.getBindingResult().getFieldError() != null
                    ? methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage()
                    : "인자값이 유효하지 않습니다.";
        }
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return constraintViolationException.getConstraintViolations().isEmpty()
                    ? "유효성 검증에 실패했습니다."
                    : constraintViolationException.getConstraintViolations().iterator().next().getMessage();
        }
        if (exception instanceof NoResourceFoundException) {
            return "요청한 리소스를 찾을 수 없습니다.";
        }
        if (exception instanceof HttpMessageNotReadableException) {
            return "요청 본문을 읽을 수 없습니다.";
        }
        if (exception instanceof IllegalArgumentException) {
            return "유효하지 않은 요청 파라미터입니다.";
        }
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return "지원되지 않는 HTTP 메서드입니다.";
        }
        if (exception instanceof ObjectOptimisticLockingFailureException) {
            return "다른 요청과 충돌이 발생했습니다. 다시 시도해주세요.";
        }
        if (exception instanceof NullPointerException) {
            return "내부 서버 오류가 발생했습니다.";
        }
        return "잘못된 요청입니다.";
    }

    private void logException(Exception exception, HttpStatus status, String message) {
        if (status.is5xxServerError()) {
            log.error("{}: {}", message, exception.getMessage(), exception);
            return;
        }
        if (status == HttpStatus.CONFLICT) {
            log.warn("{}: {}", message, exception.getMessage());
            return;
        }
        log.error("{}: {}", message, exception.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status, message));
    }
}
