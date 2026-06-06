package kr.magicbox.payment.application.service;

import kr.magicbox.payment.application.dto.command.ApprovePaymentCommand;
import kr.magicbox.payment.application.dto.result.PaymentResult;
import kr.magicbox.payment.application.dto.result.TossPaymentConfirmResult;
import kr.magicbox.payment.application.port.in.ApprovePaymentUseCase;
import kr.magicbox.payment.application.port.out.PaymentOutboxPort;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.application.port.out.TossPaymentPort;
import kr.magicbox.payment.domain.aggregate.Payment;
import kr.magicbox.payment.domain.event.PaymentFailedEvent;
import kr.magicbox.payment.domain.event.PaymentSucceededEvent;
import kr.magicbox.payment.domain.exception.PaymentAmountMismatchException;
import kr.magicbox.payment.domain.exception.PaymentConfirmDataMismatchException;
import kr.magicbox.payment.domain.exception.PaymentCustomerMismatchException;
import kr.magicbox.payment.domain.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ApprovePaymentService implements ApprovePaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final TossPaymentPort tossPaymentPort;
    private final PaymentOutboxPort paymentOutboxPort;
    private final PaymentResultMapper paymentResultMapper;

    @Override
    @Transactional
    public PaymentResult approvePayment(ApprovePaymentCommand command) {
        Payment payment = paymentRepositoryPort.findByOrderId(command.orderId())
                .orElseThrow(PaymentNotFoundException::new);
        validateApproveRequest(payment, command);

        TossPaymentConfirmResult confirmResult;
        try {
            confirmResult = tossPaymentPort.confirm(command.paymentKey(), command.orderId(), command.amount());
        } catch (RuntimeException e) {
            payment.fail();
            paymentRepositoryPort.update(payment);
            paymentOutboxPort.save(buildFailedEvent(payment, e.getMessage()));
            throw e;
        }

        validateConfirmResult(payment, confirmResult);
        payment.approve(confirmResult.paymentKey(), confirmResult.paymentMethod());
        paymentRepositoryPort.update(payment);
        paymentOutboxPort.save(buildSucceededEvent(payment));

        return paymentResultMapper.toResult(payment);
    }

    private void validateApproveRequest(Payment payment, ApprovePaymentCommand command) {
        if (!payment.getCustomerId().equals(command.customerId())) {
            throw new PaymentCustomerMismatchException();
        }
        if (payment.getTotalAmount() != command.amount()) {
            throw new PaymentAmountMismatchException();
        }
    }

    private void validateConfirmResult(Payment payment, TossPaymentConfirmResult confirmResult) {
        if (confirmResult == null) {
            throw new PaymentConfirmDataMismatchException();
        }
        if (!payment.getOrderId().equals(confirmResult.orderId()) || payment.getTotalAmount() != confirmResult.totalAmount()) {
            throw new PaymentConfirmDataMismatchException();
        }
    }

    private PaymentSucceededEvent buildSucceededEvent(Payment payment) {
        return PaymentSucceededEvent.builder()
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .paymentId(payment.getId().value())
                .totalAmount(payment.getTotalAmount())
                .approvedAt(payment.getUpdatedAt())
                .occurredAt(Instant.now())
                .build();
    }

    private PaymentFailedEvent buildFailedEvent(Payment payment, String reason) {
        return PaymentFailedEvent.builder()
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .paymentId(payment.getId().value())
                .reason(reason)
                .occurredAt(Instant.now())
                .build();
    }
}
