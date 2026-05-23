package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentAmountMismatchException extends BusinessException {

    public PaymentAmountMismatchException() {
        super("결제 금액이 주문 금액과 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
