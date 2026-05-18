package kr.magicbox.payment.adapter.in.web.dto.response;

import kr.magicbox.payment.application.dto.result.PaymentResult;
import kr.magicbox.payment.domain.enums.PaymentStatus;
import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record PaymentResponse(
        Long paymentId,
        Long orderId,
        Long customerId,
        PaymentStatus status,
        long totalAmount,
        String currency,
        String paymentKey,
        TossPaymentMethod paymentMethod,
        List<PaymentLineResponse> paymentLines,
        Instant createdAt,
        Instant updatedAt
) {
    public static PaymentResponse from(PaymentResult result) {
        return PaymentResponse.builder()
                .paymentId(result.paymentId())
                .orderId(result.orderId())
                .customerId(result.customerId())
                .status(result.status())
                .totalAmount(result.totalAmount())
                .currency(result.currency())
                .paymentKey(result.paymentKey())
                .paymentMethod(result.paymentMethod())
                .paymentLines(result.paymentLines().stream()
                        .map(PaymentLineResponse::from)
                        .toList())
                .createdAt(result.createdAt())
                .updatedAt(result.updatedAt())
                .build();
    }
}
