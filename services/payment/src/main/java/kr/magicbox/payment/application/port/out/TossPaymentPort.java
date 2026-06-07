package kr.magicbox.payment.application.port.out;

import kr.magicbox.payment.application.dto.result.TossPaymentConfirmResult;
import kr.magicbox.payment.application.dto.result.TossPaymentCancelResult;

public interface TossPaymentPort {
    TossPaymentConfirmResult confirm(String paymentKey, Long orderId, long amount);
    TossPaymentCancelResult cancel(String paymentKey, String cancelReason);
}
