package kr.magicbox.delivery.application.service;

import kr.magicbox.delivery.application.dto.query.GetDeliveryQuery;
import kr.magicbox.delivery.application.dto.result.DeliveryResult;
import kr.magicbox.delivery.application.port.in.GetDeliveryUseCase;
import kr.magicbox.delivery.application.port.out.DeliveryRepositoryPort;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import kr.magicbox.delivery.domain.exception.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetDeliveryService implements GetDeliveryUseCase {

    private final DeliveryRepositoryPort deliveryRepositoryPort;
    private final DeliveryResultMapper deliveryResultMapper;

    @Override
    @Transactional(readOnly = true)
    public DeliveryResult getDelivery(GetDeliveryQuery query) {
        Delivery delivery = deliveryRepositoryPort.findByOrderLineId(query.orderLineId())
                .orElseThrow(DeliveryNotFoundException::new);
        return deliveryResultMapper.toResult(delivery);
    }
}
