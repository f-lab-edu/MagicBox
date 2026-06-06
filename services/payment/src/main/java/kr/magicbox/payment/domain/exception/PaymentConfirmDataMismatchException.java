package kr.magicbox.payment.domain.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class PaymentConfirmDataMismatchException extends BusinessException {

    public PaymentConfirmDataMismatchException() {
        super("토스 결제 승인 응답 데이터가 주문 정보와 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
