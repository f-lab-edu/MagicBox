package kr.magicbox.payment.adapter.out.toss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TossConfirmRequest(
        @JsonProperty("paymentKey") String paymentKey,
        @JsonProperty("orderId") String orderId,
        @JsonProperty("amount") long amount
) {
}
