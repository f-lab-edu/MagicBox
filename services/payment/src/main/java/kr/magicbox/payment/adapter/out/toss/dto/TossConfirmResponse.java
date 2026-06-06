package kr.magicbox.payment.adapter.out.toss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TossConfirmResponse(
        @JsonProperty("paymentKey") String paymentKey,
        @JsonProperty("orderId") String orderId,
        @JsonProperty("method") String method,
        @JsonProperty("totalAmount") long totalAmount,
        @JsonProperty("status") String status,
        @JsonProperty("approvedAt") String approvedAt
) {
}
