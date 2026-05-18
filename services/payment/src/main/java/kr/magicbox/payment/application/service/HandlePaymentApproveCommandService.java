package kr.magicbox.payment.application.service;

import kr.magicbox.payment.application.port.in.HandlePaymentApproveCommandUseCase;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.domain.aggregate.Payment;
import kr.magicbox.payment.domain.exception.PaymentAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandlePaymentApproveCommandService implements HandlePaymentApproveCommandUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;

    @Override
    @Transactional
    public void handlePaymentApproveCommand(Long orderId, Long customerId, long totalAmount) {
        if (paymentRepositoryPort.findByOrderId(orderId).isPresent()) {
            throw new PaymentAlreadyExistsException();
        }

        Payment payment = Payment.createBuilder()
                .orderId(orderId)
                .customerId(customerId)
                .totalAmount(totalAmount)
                .build();

        paymentRepositoryPort.save(payment);
    }
}
