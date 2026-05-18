package kr.magicbox.payment.application.port.in;

public interface HandlePaymentApproveCommandUseCase {
    void handlePaymentApproveCommand(Long orderId, Long customerId, long totalAmount);
}
