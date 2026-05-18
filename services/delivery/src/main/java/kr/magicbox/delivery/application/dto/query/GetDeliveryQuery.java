package kr.magicbox.delivery.application.dto.query;

import lombok.Builder;

@Builder
public record GetDeliveryQuery(
        Long orderLineId
) {
}
