package kr.magicbox.payment.application.dto.result;

import kr.magicbox.payment.domain.enums.PaymentStatus;
import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record PaymentResult(
        Long paymentId,
        Long orderId,
        Long customerId,
        PaymentStatus status,
        long totalAmount,
        String currency,
        String paymentKey,
        TossPaymentMethod paymentMethod,
        Instant createdAt,
        Instant updatedAt,
        List<PaymentLineResult> paymentLines
) {
    @Builder
    public record PaymentLineResult(
            Long paymentLineId,
            Long orderLineId,
            Long sellerId,
            long amount
    ) {
    }
}
