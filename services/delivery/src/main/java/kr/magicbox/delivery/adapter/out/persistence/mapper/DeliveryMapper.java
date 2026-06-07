package kr.magicbox.delivery.adapter.out.persistence.mapper;

import kr.magicbox.delivery.adapter.out.persistence.entity.DeliveryEntity;
import kr.magicbox.delivery.adapter.out.persistence.entity.TrackingHistoryEntity;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import kr.magicbox.delivery.domain.aggregate.TrackingHistory;
import kr.magicbox.delivery.domain.vo.DeliveryId;
import kr.magicbox.delivery.domain.vo.TrackingHistoryId;
import kr.magicbox.delivery.domain.vo.TrackingInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeliveryMapper {

    public DeliveryEntity toEntity(Delivery delivery) {
        return DeliveryEntity.builder()
                .orderLineId(delivery.getOrderLineId())
                .orderId(delivery.getOrderId())
                .customerId(delivery.getCustomerId())
                .status(delivery.getStatus())
                .carrierCode(delivery.getTrackingInfo().carrierCode())
                .trackingNumber(delivery.getTrackingInfo().trackingNumber())
                .build();
    }

    public TrackingHistoryEntity toHistoryEntity(Long deliveryId, TrackingHistory history) {
        return TrackingHistoryEntity.builder()
                .deliveryId(deliveryId)
                .status(history.getStatus())
                .description(history.getDescription())
                .trackedAt(history.getTrackedAt())
                .build();
    }

    public Delivery toDomain(DeliveryEntity entity, List<TrackingHistoryEntity> historyEntities) {
        return Delivery.reconstructBuilder()
                .id(DeliveryId.of(entity.getId()))
                .orderLineId(entity.getOrderLineId())
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus())
                .trackingInfo(TrackingInfo.of(entity.getCarrierCode(), entity.getTrackingNumber()))
                .trackingHistories(historyEntities.stream().map(this::toHistoryDomain).toList())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private TrackingHistory toHistoryDomain(TrackingHistoryEntity entity) {
        return TrackingHistory.reconstructBuilder()
                .id(TrackingHistoryId.of(entity.getId()))
                .status(entity.getStatus())
                .description(entity.getDescription())
                .trackedAt(entity.getTrackedAt())
                .build();
    }
}
