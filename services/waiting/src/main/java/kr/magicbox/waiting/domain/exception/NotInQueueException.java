package kr.magicbox.waiting.domain.exception;

import kr.magicbox.waiting.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class NotInQueueException extends BusinessException {

    public NotInQueueException() {
        super("대기열에 등록되어 있지 않습니다.", HttpStatus.NOT_FOUND);
    }
}
