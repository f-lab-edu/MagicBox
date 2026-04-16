package kr.magicbox.subscribe.domain.exception;

import kr.magicbox.subscribe.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class SubscriptionNotFoundException extends BusinessException {
    public SubscriptionNotFoundException() {
        super("구독 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
