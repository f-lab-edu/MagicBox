package kr.magicbox.payment.application.dto.command;

import lombok.Builder;

@Builder
public record CancelPaymentCommand(
        Long orderId,
        Long customerId,
        String cancelReason
) {
}
