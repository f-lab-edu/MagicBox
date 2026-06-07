package kr.magicbox.settlement.adapter.out.persistence.mapper;

import kr.magicbox.settlement.adapter.out.persistence.entity.SettlementEntity;
import kr.magicbox.settlement.domain.aggregate.Settlement;
import kr.magicbox.settlement.domain.vo.CreatorAccount;
import kr.magicbox.settlement.domain.vo.SettlementId;
import org.springframework.stereotype.Component;

@Component
public class SettlementMapper {

    public SettlementEntity toEntity(Settlement settlement) {
        CreatorAccount account = settlement.getCreatorAccount();
        return SettlementEntity.builder()
                .orderId(settlement.getOrderId())
                .orderLineId(settlement.getOrderLineId())
                .creatorId(settlement.getCreatorId())
                .status(settlement.getStatus())
                .grossAmount(settlement.getGrossAmount())
                .fee(settlement.getFee())
                .netAmount(settlement.getNetAmount())
                .bankCode(account != null ? account.bankCode() : null)
                .accountNumber(account != null ? account.accountNumber() : null)
                .accountHolder(account != null ? account.accountHolder() : null)
                .settledAt(settlement.getSettledAt())
                .build();
    }

    public Settlement toDomain(SettlementEntity entity) {
        CreatorAccount account = null;
        if (entity.getBankCode() != null && entity.getAccountNumber() != null && entity.getAccountHolder() != null) {
            account = CreatorAccount.of(entity.getBankCode(), entity.getAccountNumber(), entity.getAccountHolder());
        }
        return Settlement.reconstructBuilder()
                .id(SettlementId.of(entity.getId()))
                .orderId(entity.getOrderId())
                .orderLineId(entity.getOrderLineId())
                .creatorId(entity.getCreatorId())
                .status(entity.getStatus())
                .grossAmount(entity.getGrossAmount())
                .fee(entity.getFee())
                .netAmount(entity.getNetAmount())
                .creatorAccount(account)
                .settledAt(entity.getSettledAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
