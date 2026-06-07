package kr.magicbox.payment.application.port.in;

import kr.magicbox.payment.application.dto.command.ApprovePaymentCommand;
import kr.magicbox.payment.application.dto.result.PaymentResult;

public interface ApprovePaymentUseCase {
    PaymentResult approvePayment(ApprovePaymentCommand command);
}
