package kr.magicbox.payment.domain.vo;

import kr.magicbox.payment.domain.exception.InvalidFieldException;

public record PaymentLineId(Long value) {

    public PaymentLineId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("결제 라인 ID는 양수여야 합니다.");
        }
    }

    public static PaymentLineId of(Long value) {
        return new PaymentLineId(value);
    }
}
