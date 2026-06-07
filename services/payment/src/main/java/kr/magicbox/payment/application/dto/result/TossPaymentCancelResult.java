package kr.magicbox.payment.application.dto.result;

import lombok.Builder;

import java.time.Instant;

@Builder
public record TossPaymentCancelResult(
        String paymentKey,
        Instant cancelledAt
) {
}
