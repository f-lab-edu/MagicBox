package kr.magicbox.payment.adapter.out.toss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TossConfirmResponse(
        String paymentKey,
        String orderId,
        String method,
        long totalAmount,
        String status,
        @JsonProperty("approvedAt") String approvedAt
) {
}
