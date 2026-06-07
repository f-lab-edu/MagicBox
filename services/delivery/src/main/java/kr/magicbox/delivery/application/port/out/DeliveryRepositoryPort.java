package kr.magicbox.delivery.application.port.out;

import kr.magicbox.delivery.domain.aggregate.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepositoryPort {
    Delivery save(Delivery delivery);

    void update(Delivery delivery);

    boolean existsByOrderLineId(Long orderLineId);

    Optional<Delivery> findByOrderLineId(Long orderLineId);

    Optional<Delivery> findById(Long deliveryId);

    List<Delivery> findAllInProgress();
}
