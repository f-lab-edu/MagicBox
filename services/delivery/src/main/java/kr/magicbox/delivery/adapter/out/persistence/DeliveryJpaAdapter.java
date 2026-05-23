package kr.magicbox.delivery.adapter.out.persistence;

import kr.magicbox.delivery.adapter.out.persistence.entity.DeliveryEntity;
import kr.magicbox.delivery.adapter.out.persistence.mapper.DeliveryMapper;
import kr.magicbox.delivery.adapter.out.persistence.repository.DeliveryJpaRepository;
import kr.magicbox.delivery.adapter.out.persistence.repository.TrackingHistoryJpaRepository;
import kr.magicbox.delivery.application.port.out.DeliveryRepositoryPort;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import kr.magicbox.delivery.domain.exception.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryJpaAdapter implements DeliveryRepositoryPort {

    private final DeliveryJpaRepository deliveryJpaRepository;
    private final TrackingHistoryJpaRepository trackingHistoryJpaRepository;
    private final DeliveryMapper deliveryMapper;

    @Override
    public Delivery save(Delivery delivery) {
        DeliveryEntity savedEntity = deliveryJpaRepository.save(deliveryMapper.toEntity(delivery));
        delivery.getTrackingHistories().forEach(history ->
                trackingHistoryJpaRepository.save(deliveryMapper.toHistoryEntity(savedEntity.getId(), history)));
        return findByOrderLineId(savedEntity.getOrderLineId()).orElseThrow(DeliveryNotFoundException::new);
    }

    @Override
    public void update(Delivery delivery) {
        DeliveryEntity entity = deliveryJpaRepository.findByIdAndIsDeletedFalse(delivery.getId().value())
                .orElseThrow(DeliveryNotFoundException::new);
        entity.update(delivery.getStatus(), delivery.getTrackingInfo().carrierCode(), delivery.getTrackingInfo().trackingNumber());
        deliveryJpaRepository.save(entity);
        trackingHistoryJpaRepository.deleteByDeliveryId(entity.getId());
        delivery.getTrackingHistories().forEach(history ->
                trackingHistoryJpaRepository.save(deliveryMapper.toHistoryEntity(entity.getId(), history)));
    }

    @Override
    public boolean existsByOrderLineId(Long orderLineId) {
        return deliveryJpaRepository.existsByOrderLineIdAndIsDeletedFalse(orderLineId);
    }

    @Override
    public Optional<Delivery> findByOrderLineId(Long orderLineId) {
        return deliveryJpaRepository.findByOrderLineIdAndIsDeletedFalse(orderLineId)
                .map(entity -> deliveryMapper.toDomain(
                        entity,
                        trackingHistoryJpaRepository.findByDeliveryId(entity.getId()))
                );
    }

    @Override
    public Optional<Delivery> findById(Long deliveryId) {
        return deliveryJpaRepository.findByIdAndIsDeletedFalse(deliveryId)
                .map(entity -> deliveryMapper.toDomain(
                        entity,
                        trackingHistoryJpaRepository.findByDeliveryId(entity.getId()))
                );
    }

    @Override
    public List<Delivery> findAllInProgress() {
        return deliveryJpaRepository.findAllInProgress().stream()
                .map(entity -> deliveryMapper.toDomain(entity, trackingHistoryJpaRepository.findByDeliveryId(entity.getId())))
                .toList();
    }
}
