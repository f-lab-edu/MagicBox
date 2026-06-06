package kr.magicbox.delivery.application.dto.command;

import lombok.Builder;

@Builder
public record StartDeliveryCommand(
        Long orderLineId,
        Long orderId,
        Long sellerId,
        Long customerId,
        String carrierCode,
        String trackingNumber
) {
}
