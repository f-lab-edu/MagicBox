package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentAlreadyExistsException extends BusinessException {

    public PaymentAlreadyExistsException() {
        super("이미 처리된 결제입니다.", HttpStatus.CONFLICT);
    }
}
