package kr.magicbox.payment.application.service;

import kr.magicbox.payment.adapter.in.kafka.event.PaymentApproveCommandEvent;
import kr.magicbox.payment.application.port.in.HandlePaymentApproveCommandUseCase;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.domain.aggregate.Payment;
import kr.magicbox.payment.domain.aggregate.PaymentLine;
import kr.magicbox.payment.domain.exception.PaymentAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HandlePaymentApproveCommandService implements HandlePaymentApproveCommandUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    @Transactional
    public void handlePaymentApproveCommand(Long orderId, Long customerId, long totalAmount, List<PaymentApproveCommandEvent.ItemPayload> items) {
        if (paymentRepositoryPort.findByOrderId(orderId).isPresent()) {
            throw new PaymentAlreadyExistsException();
        }

        List<PaymentLine> paymentLines = items == null ? List.of() : items.stream()
                .map(item -> PaymentLine.createBuilder()
                        .orderLineId(item.orderLineId())
                        .sellerId(item.sellerId())
                        .amount(item.amount())
                        .build())
                .toList();

        Payment payment = Payment.createBuilder()
                .orderId(orderId)
                .customerId(customerId)
                .totalAmount(totalAmount)
                .paymentLines(paymentLines)
                .build();

        paymentRepositoryPort.save(payment);
    }
}
