package kr.magicbox.payment.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentDomainEventType {
    PAYMENT_SUCCEEDED("payment-succeeded"),
    PAYMENT_FAILED("payment-failed"),
    PAYMENT_CANCEL_SUCCEEDED("payment-cancel-succeeded"),
    PAYMENT_CANCEL_FAILED("payment-cancel-failed");

    private final String value;
}
