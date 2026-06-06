package kr.magicbox.delivery.application.dto.result;

import kr.magicbox.delivery.domain.enums.DeliveryStatus;
import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record DeliveryResult(
        Long deliveryId,
        Long orderLineId,
        Long orderId,
        DeliveryStatus status,
        String carrierCode,
        String trackingNumber,
        Instant createdAt,
        Instant updatedAt,
        List<TrackingHistoryResult> trackingHistories
) {
    @Builder
    public record TrackingHistoryResult(
            Long trackingHistoryId,
            TrackingHistoryStatus status,
            String description,
            Instant trackedAt
    ) {
    }
}
