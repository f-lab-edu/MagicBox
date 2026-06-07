package kr.magicbox.payment.application.port.in;

public interface HandlePaymentCancelCommandUseCase {
    void handlePaymentCancelCommand(Long orderId, Long customerId);
}
