package kr.magicbox.settlement.application.port.in;

public interface HandleSettlementSettleCommandUseCase {
    void handleSettlementSettleCommand(Long orderId, Long orderLineId, Long sellerId, long grossAmount);
}
