package kr.magicbox.delivery.application.service;

import kr.magicbox.delivery.application.dto.command.StartDeliveryCommand;
import kr.magicbox.delivery.application.dto.result.DeliveryResult;
import kr.magicbox.delivery.application.port.in.StartDeliveryUseCase;
import kr.magicbox.delivery.application.port.out.DeliveryOutboxPort;
import kr.magicbox.delivery.application.port.out.DeliveryRepositoryPort;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import kr.magicbox.delivery.domain.aggregate.TrackingHistory;
import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import kr.magicbox.delivery.domain.event.DeliveryStartedEvent;
import kr.magicbox.delivery.domain.exception.DeliveryAlreadyStartedException;
import kr.magicbox.delivery.domain.vo.TrackingInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartDeliveryService implements StartDeliveryUseCase {

    private final DeliveryRepositoryPort deliveryRepositoryPort;
    private final DeliveryOutboxPort deliveryOutboxPort;
    private final DeliveryResultMapper deliveryResultMapper;

    @Override
    @Transactional
    public DeliveryResult startDelivery(StartDeliveryCommand command) {
        if (deliveryRepositoryPort.existsByOrderLineId(command.orderLineId())) {
            throw new DeliveryAlreadyStartedException();
        }

        Delivery delivery = Delivery.createBuilder()
                .orderLineId(command.orderLineId())
                .orderId(command.orderId())
                .customerId(command.customerId())
                .trackingInfo(TrackingInfo.of(command.carrierCode(), command.trackingNumber()))
                .build();

        delivery.addHistory(TrackingHistory.createBuilder()
                .status(TrackingHistoryStatus.SHIPPED)
                .description("배송이 시작되었습니다.")
                .trackedAt(Instant.now())
                .build());

        Delivery persistedDelivery = deliveryRepositoryPort.save(delivery);

        Instant now = Instant.now();
        deliveryOutboxPort.save(DeliveryStartedEvent.builder()
                .orderId(persistedDelivery.getOrderId())
                .orderLineId(persistedDelivery.getOrderLineId())
                .customerId(command.customerId())
                .deliveryId(persistedDelivery.getId().value())
                .carrierCode(command.carrierCode())
                .trackingNumber(command.trackingNumber())
                .dispatchedAt(now)
                .occurredAt(now)
                .build());

        log.info("[Delivery] 배송 시작 처리 완료. orderLineId={}, orderId={}, deliveryId={}",
                persistedDelivery.getOrderLineId(), persistedDelivery.getOrderId(), persistedDelivery.getId().value());

        return deliveryResultMapper.toResult(persistedDelivery);
    }
}
