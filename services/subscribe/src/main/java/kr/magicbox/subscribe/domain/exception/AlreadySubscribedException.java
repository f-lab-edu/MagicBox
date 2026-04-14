package kr.magicbox.subscribe.domain.exception;

import kr.magicbox.subscribe.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class AlreadySubscribedException extends BusinessException {
    public AlreadySubscribedException() {
        super("이미 구독 중입니다.", HttpStatus.CONFLICT);
    }
}
