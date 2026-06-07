package kr.magicbox.delivery.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.magicbox.delivery.application.dto.command.StartDeliveryCommand;

public record StartDeliveryRequest(
        @NotNull(message = "주문 ID는 필수입니다.")
        @Positive(message = "주문 ID는 양수여야 합니다.")
        Long orderId,

        @NotNull(message = "구매자 ID는 필수입니다.")
        @Positive(message = "구매자 ID는 양수여야 합니다.")
        Long customerId,

        @NotBlank(message = "택배사 코드는 필수입니다.")
        String carrierCode,

        @NotBlank(message = "운송장 번호는 필수입니다.")
        String trackingNumber
) {
    public StartDeliveryCommand toCommand(Long orderLineId, Long sellerId) {
        return StartDeliveryCommand.builder()
                .orderLineId(orderLineId)
                .orderId(orderId)
                .sellerId(sellerId)
                .customerId(customerId)
                .carrierCode(carrierCode)
                .trackingNumber(trackingNumber)
                .build();
    }
}
