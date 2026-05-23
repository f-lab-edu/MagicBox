package kr.magicbox.payment.application.service;

import kr.magicbox.payment.application.dto.command.CancelPaymentCommand;
import kr.magicbox.payment.application.port.in.CancelPaymentUseCase;
import kr.magicbox.payment.application.port.out.PaymentOutboxPort;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.application.port.out.TossPaymentPort;
import kr.magicbox.payment.domain.aggregate.Payment;
import kr.magicbox.payment.domain.event.PaymentCancelFailedEvent;
import kr.magicbox.payment.domain.event.PaymentCancelSucceededEvent;
import kr.magicbox.payment.domain.exception.PaymentCustomerMismatchException;
import kr.magicbox.payment.domain.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CancelPaymentService implements CancelPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final TossPaymentPort tossPaymentPort;
    private final PaymentOutboxPort paymentOutboxPort;

    @Override
    @Transactional
    public void cancelPayment(CancelPaymentCommand command) {
        Payment payment = paymentRepositoryPort.findByOrderId(command.orderId())
                .orElseThrow(PaymentNotFoundException::new);
        if (!payment.getCustomerId().equals(command.customerId())) {
            throw new PaymentCustomerMismatchException();
        }

        payment.requestCancel();

        try {
            tossPaymentPort.cancel(payment.getPaymentKey(), command.cancelReason());
        } catch (RuntimeException e) {
            payment.failCancel();
            paymentRepositoryPort.update(payment);
            paymentOutboxPort.save(PaymentCancelFailedEvent.builder()
                    .orderId(payment.getOrderId())
                    .customerId(payment.getCustomerId())
                    .paymentId(payment.getId().value())
                    .occurredAt(Instant.now())
                    .build());
            throw e;
        }

        payment.completeCancel();
        paymentRepositoryPort.update(payment);
        paymentOutboxPort.save(PaymentCancelSucceededEvent.builder()
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .paymentId(payment.getId().value())
                .cancelledAt(payment.getUpdatedAt())
                .occurredAt(Instant.now())
                .build());
    }
}
