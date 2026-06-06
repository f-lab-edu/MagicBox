package kr.magicbox.payment.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import kr.magicbox.payment.application.dto.command.ApprovePaymentCommand;
import lombok.Builder;

@Builder
public record ApprovePaymentRequest(
        @NotBlank(message = "결제 키는 필수입니다.") String paymentKey,
        @Positive(message = "결제 금액은 양수여야 합니다.") long amount
) {
    public ApprovePaymentCommand toCommand(Long orderId, Long customerId) {
        return ApprovePaymentCommand.builder()
                .orderId(orderId)
                .customerId(customerId)
                .paymentKey(paymentKey)
                .amount(amount)
                .build();
    }
}
