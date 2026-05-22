package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.ConfirmOrderLineUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.enums.OrderStatus;
import kr.magicbox.order.domain.event.OrderConfirmedEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmOrderLineService implements ConfirmOrderLineUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void confirmOrderLine(Long orderId, Long orderLineId, Long sellerId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));

        if (!order.getSellerId().equals(sellerId)) {
            throw new OrderUnauthorizedException();
        }

        OrderStatus previousStatus = order.getStatus();
        order.confirmOrderLine(orderLineId);
        orderRepositoryPort.update(order);

        // PREPARING → CONFIRMED 전이 시에만 이벤트 1회 발행 (이미 CONFIRMED였던 경우 중복 발행 방지)
        if (previousStatus != OrderStatus.CONFIRMED && order.getStatus() == OrderStatus.CONFIRMED) {
            orderOutboxPort.save(OrderConfirmedEvent.from(order));
        }
    }
}
