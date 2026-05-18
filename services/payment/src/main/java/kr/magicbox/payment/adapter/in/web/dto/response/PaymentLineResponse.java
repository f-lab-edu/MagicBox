package kr.magicbox.payment.adapter.in.web.dto.response;

import kr.magicbox.payment.application.dto.result.PaymentResult;
import lombok.Builder;

@Builder
public record PaymentLineResponse(
        Long paymentLineId,
        Long orderLineId,
        Long sellerId,
        long amount
) {
    public static PaymentLineResponse from(PaymentResult.PaymentLineResult result) {
        return PaymentLineResponse.builder()
                .paymentLineId(result.paymentLineId())
                .orderLineId(result.orderLineId())
                .sellerId(result.sellerId())
                .amount(result.amount())
                .build();
    }
}
