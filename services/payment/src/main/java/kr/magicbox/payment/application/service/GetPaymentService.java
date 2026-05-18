package kr.magicbox.payment.application.service;

import kr.magicbox.payment.application.dto.result.PaymentResult;
import kr.magicbox.payment.application.port.in.GetPaymentUseCase;
import kr.magicbox.payment.application.port.out.PaymentRepositoryPort;
import kr.magicbox.payment.domain.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPaymentService implements GetPaymentUseCase {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final PaymentResultMapper paymentResultMapper;

    @Override
    @Transactional(readOnly = true)
    public PaymentResult getPayment(Long orderId) {
        return paymentRepositoryPort.findByOrderId(orderId)
                .map(paymentResultMapper::toResult)
                .orElseThrow(PaymentNotFoundException::new);
    }
}
