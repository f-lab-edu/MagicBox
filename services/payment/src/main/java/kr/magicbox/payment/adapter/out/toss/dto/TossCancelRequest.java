package kr.magicbox.payment.adapter.out.toss.dto;

import lombok.Builder;

@Builder
public record TossCancelRequest(
        String cancelReason
) {
}
