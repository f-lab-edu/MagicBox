package kr.magicbox.payment.adapter.out.toss.dto;

import lombok.Builder;

@Builder
public record TossConfirmRequest(
        String paymentKey,
        String orderId,
        long amount
) {
}
