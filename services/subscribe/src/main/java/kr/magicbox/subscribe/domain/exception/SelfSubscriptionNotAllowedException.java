package kr.magicbox.subscribe.domain.exception;

import kr.magicbox.subscribe.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class SelfSubscriptionNotAllowedException extends BusinessException {
    public SelfSubscriptionNotAllowedException() {
        super("자기 자신은 구독할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
