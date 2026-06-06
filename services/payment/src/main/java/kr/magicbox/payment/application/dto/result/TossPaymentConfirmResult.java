package kr.magicbox.payment.application.dto.result;

import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import lombok.Builder;

@Builder
public record TossPaymentConfirmResult(
        String paymentKey,
        TossPaymentMethod paymentMethod,
        Long orderId,
        long totalAmount
) {
}
