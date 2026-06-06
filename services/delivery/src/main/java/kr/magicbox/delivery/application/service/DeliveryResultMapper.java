package kr.magicbox.delivery.application.service;

import kr.magicbox.delivery.application.dto.result.DeliveryResult;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryResultMapper {

    public DeliveryResult toResult(Delivery delivery) {
        return DeliveryResult.builder()
                .deliveryId(delivery.getId().value())
                .orderLineId(delivery.getOrderLineId())
                .orderId(delivery.getOrderId())
                .status(delivery.getStatus())
                .carrierCode(delivery.getTrackingInfo().carrierCode())
                .trackingNumber(delivery.getTrackingInfo().trackingNumber())
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .trackingHistories(delivery.getTrackingHistories().stream()
                        .map(history -> DeliveryResult.TrackingHistoryResult.builder()
                                .trackingHistoryId(history.getId() != null ? history.getId().value() : null)
                                .status(history.getStatus())
                                .description(history.getDescription())
                                .trackedAt(history.getTrackedAt())
                                .build())
                        .toList())
                .build();
    }
}
