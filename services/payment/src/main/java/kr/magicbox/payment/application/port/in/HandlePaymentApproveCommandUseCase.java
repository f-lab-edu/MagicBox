package kr.magicbox.payment.application.port.in;

import kr.magicbox.payment.adapter.in.kafka.event.PaymentApproveCommandEvent;

import java.util.List;

public interface HandlePaymentApproveCommandUseCase {
    void handlePaymentApproveCommand(Long orderId, Long customerId, long totalAmount, List<PaymentApproveCommandEvent.ItemPayload> items);
}
