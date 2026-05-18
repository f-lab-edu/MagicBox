package kr.magicbox.delivery.domain.exception;

import kr.magicbox.delivery.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class DeliveryNotFoundException extends BusinessException {

    public DeliveryNotFoundException() {
        super("배송 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
