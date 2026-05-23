package kr.magicbox.delivery.domain.exception;

import kr.magicbox.delivery.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryAlreadyStartedException extends BusinessException {

    public DeliveryAlreadyStartedException() {
        super("이미 배송이 시작된 주문입니다.", HttpStatus.CONFLICT);
    }
}
