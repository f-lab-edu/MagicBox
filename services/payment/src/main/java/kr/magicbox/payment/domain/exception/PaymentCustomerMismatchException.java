package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentCustomerMismatchException extends BusinessException {

    public PaymentCustomerMismatchException() {
        super("결제 요청 사용자와 결제 소유자가 일치하지 않습니다.", HttpStatus.FORBIDDEN);
    }
}
