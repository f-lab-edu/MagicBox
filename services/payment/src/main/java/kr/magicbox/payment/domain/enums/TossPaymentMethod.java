package kr.magicbox.payment.domain.enums;

import kr.magicbox.payment.domain.exception.InvalidFieldException;

public enum TossPaymentMethod {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAY("간편결제"),
    BANK_TRANSFER("계좌이체"),
    MOBILE_PHONE("휴대폰"),
    GIFT_CERTIFICATE("상품권"),
    FOREIGN_EASY_PAY("해외간편결제");

    private final String tossValue;

    TossPaymentMethod(String tossValue) {
        this.tossValue = tossValue;
    }

    public static TossPaymentMethod fromTossMethod(String method) {
        for (TossPaymentMethod value : values()) {
            if (value.tossValue.equals(method)) {
                return value;
            }
        }
        throw new InvalidFieldException("지원하지 않는 결제 수단입니다: " + method);
    }
}
