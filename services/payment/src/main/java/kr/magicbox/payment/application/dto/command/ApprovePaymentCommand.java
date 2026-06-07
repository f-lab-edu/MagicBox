package kr.magicbox.payment.application.dto.command;

import lombok.Builder;

@Builder
public record ApprovePaymentCommand(
        Long orderId,
        Long customerId,
        String paymentKey,
        long amount
) {
}
