package kr.magicbox.payment.domain.vo;

import kr.magicbox.payment.domain.exception.InvalidFieldException;

public record PaymentId(Long value) {

    public PaymentId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("결제 ID는 양수여야 합니다.");
        }
    }

    public static PaymentId of(Long value) {
        return new PaymentId(value);
    }
}
