package kr.magicbox.settlement.adapter.out.persistence;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementEntity;
import kr.magicbox.settlement.adapter.out.persistence.mapper.SettlementMapper;
import kr.magicbox.settlement.adapter.out.persistence.repository.SettlementJpaRepository;
import kr.magicbox.settlement.application.port.out.SettlementRepositoryPort;
import kr.magicbox.settlement.domain.aggregate.Settlement;
import kr.magicbox.settlement.domain.exception.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SettlementJpaAdapter implements SettlementRepositoryPort {

    private final SettlementJpaRepository settlementJpaRepository;
    private final SettlementMapper settlementMapper;

    @Override
    public Settlement save(Settlement settlement) {
        SettlementEntity saved = settlementJpaRepository.save(settlementMapper.toEntity(settlement));
        return settlementMapper.toDomain(saved);
    }

    @Override
    public void update(Settlement settlement) {
        SettlementEntity entity = settlementJpaRepository.findById(settlement.getId().value())
                .orElseThrow(() -> new InvalidFieldException("정산 정보를 찾을 수 없습니다."));
        entity.update(
                settlement.getStatus(),
                settlement.getCreatorId(),
                settlement.getGrossAmount(),
                settlement.getFee(),
                settlement.getNetAmount(),
                settlement.getCreatorAccount() != null ? settlement.getCreatorAccount().bankCode() : null,
                settlement.getCreatorAccount() != null ? settlement.getCreatorAccount().accountNumber() : null,
                settlement.getCreatorAccount() != null ? settlement.getCreatorAccount().accountHolder() : null,
                settlement.getSettledAt()
        );
    }

    @Override
    public Optional<Settlement> findByOrderLineId(Long orderLineId) {
        return settlementJpaRepository.findByOrderLineId(orderLineId)
                .map(settlementMapper::toDomain);
    }

    @Override
    public List<Settlement> findByOrderId(Long orderId) {
        return settlementJpaRepository.findByOrderId(orderId).stream()
                .map(settlementMapper::toDomain)
                .toList();
    }
}
