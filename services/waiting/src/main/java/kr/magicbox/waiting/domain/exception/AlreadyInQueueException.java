package kr.magicbox.waiting.domain.exception;

import kr.magicbox.waiting.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class AlreadyInQueueException extends BusinessException {

    public AlreadyInQueueException() {
        super("이미 대기열에 등록되어 있습니다.", HttpStatus.CONFLICT);
    }
}
