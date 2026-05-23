package kr.magicbox.settlement.application.port.out;

import kr.magicbox.settlement.domain.aggregate.Settlement;

import java.util.List;
import java.util.Optional;

public interface SettlementRepositoryPort {
    Settlement save(Settlement settlement);
    void update(Settlement settlement);
    Optional<Settlement> findByOrderLineId(Long orderLineId);
    List<Settlement> findByOrderId(Long orderId);
}
