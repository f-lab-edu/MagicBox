package kr.magicbox.delivery.adapter.in.web.dto.response;

import kr.magicbox.delivery.application.dto.result.DeliveryResult;
import kr.magicbox.delivery.domain.enums.DeliveryStatus;
import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record DeliveryResponse(
        Long deliveryId,
        Long orderLineId,
        Long orderId,
        DeliveryStatus status,
        String carrierCode,
        String trackingNumber,
        Instant createdAt,
        Instant updatedAt,
        List<TrackingHistoryResponse> trackingHistories
) {
    public static DeliveryResponse from(DeliveryResult result) {
        return DeliveryResponse.builder()
                .deliveryId(result.deliveryId())
                .orderLineId(result.orderLineId())
                .orderId(result.orderId())
                .status(result.status())
                .carrierCode(result.carrierCode())
                .trackingNumber(result.trackingNumber())
                .createdAt(result.createdAt())
                .updatedAt(result.updatedAt())
                .trackingHistories(result.trackingHistories().stream()
                        .map(TrackingHistoryResponse::from)
                        .toList())
                .build();
    }

    @Builder
    public record TrackingHistoryResponse(
            Long trackingHistoryId,
            TrackingHistoryStatus status,
            String description,
            Instant trackedAt
    ) {
        public static TrackingHistoryResponse from(DeliveryResult.TrackingHistoryResult result) {
            return TrackingHistoryResponse.builder()
                    .trackingHistoryId(result.trackingHistoryId())
                    .status(result.status())
                    .description(result.description())
                    .trackedAt(result.trackedAt())
                    .build();
        }
    }
}
