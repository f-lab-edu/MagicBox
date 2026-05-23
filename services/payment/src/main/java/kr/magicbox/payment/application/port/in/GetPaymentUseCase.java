package kr.magicbox.payment.application.port.in;

import kr.magicbox.payment.application.dto.result.PaymentResult;

public interface GetPaymentUseCase {
    PaymentResult getPayment(Long orderId);
}
