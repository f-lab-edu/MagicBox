package kr.magicbox.settlement.application.port.in;

public interface HandleSettlementReadyCommandUseCase {
    void handleSettlementReadyCommand(Long orderId, Long orderLineId);
}
