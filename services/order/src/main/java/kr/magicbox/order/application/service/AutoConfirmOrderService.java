package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.AutoConfirmOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderAutoConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoConfirmOrderService implements AutoConfirmOrderUseCase {

    private static final int AUTO_CONFIRM_DAYS = 7;

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void autoConfirmDeliveredOrders() {
        Instant threshold = Instant.now().minus(AUTO_CONFIRM_DAYS, ChronoUnit.DAYS);
        List<Order> orders = orderRepositoryPort.findDeliveredBefore(threshold);

        for (Order order : orders) {
            order.confirmPurchase();
            orderRepositoryPort.update(order);
            OrderAutoConfirmedEvent.fromShippedLines(order).forEach(orderOutboxPort::save);
            log.info("[AutoConfirm] 자동 구매 확정 처리. orderId={}", order.getId().value());
        }
    }
}
