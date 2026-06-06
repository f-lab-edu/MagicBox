package kr.magicbox.payment.adapter.out.toss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TossCancelResponse(
        String paymentKey,
        String status,
        @JsonProperty("cancels") Object cancels
) {
}
