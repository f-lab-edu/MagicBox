package kr.magicbox.payment.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.payment.application.dto.command.CancelPaymentCommand;
import lombok.Builder;

@Builder
public record CancelPaymentRequest(
        @NotBlank(message = "취소 사유는 필수입니다.") String cancelReason
) {
    public CancelPaymentCommand toCommand(Long orderId, Long customerId) {
        return CancelPaymentCommand.builder()
                .orderId(orderId)
                .customerId(customerId)
                .cancelReason(cancelReason)
                .build();
    }
}
