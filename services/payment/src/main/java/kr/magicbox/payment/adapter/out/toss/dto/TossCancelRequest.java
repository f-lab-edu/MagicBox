package kr.magicbox.payment.adapter.out.toss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record TossCancelRequest(
        @JsonProperty("cancelReason") String cancelReason
) {
}
