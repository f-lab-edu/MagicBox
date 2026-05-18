package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException() {
        super("결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
