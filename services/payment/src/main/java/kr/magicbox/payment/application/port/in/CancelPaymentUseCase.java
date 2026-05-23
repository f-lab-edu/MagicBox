package kr.magicbox.payment.application.port.in;

import kr.magicbox.payment.application.dto.command.CancelPaymentCommand;

public interface CancelPaymentUseCase {
    void cancelPayment(CancelPaymentCommand command);
}
