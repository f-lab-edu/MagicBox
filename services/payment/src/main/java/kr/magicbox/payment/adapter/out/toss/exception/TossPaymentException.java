package kr.magicbox.payment.adapter.out.toss.exception;

import kr.magicbox.payment.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class TossPaymentException extends BusinessException {

    public TossPaymentException(String code, String message) {
        super("[" + code + "] " + message, HttpStatus.BAD_REQUEST);
    }
}
