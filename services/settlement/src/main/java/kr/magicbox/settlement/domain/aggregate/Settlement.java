package kr.magicbox.settlement.domain.aggregate;

import kr.magicbox.settlement.domain.enums.SettlementStatus;
import kr.magicbox.settlement.domain.exception.InvalidFieldException;
import kr.magicbox.settlement.domain.vo.CreatorAccount;
import kr.magicbox.settlement.domain.vo.SettlementId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Settlement {

    private final SettlementId id;
    private final Long orderId;
    private final Long orderLineId;
    private Long creatorId;
    private SettlementStatus status;
    private Long grossAmount;
    private Long fee;
    private Long netAmount;
    private CreatorAccount creatorAccount;
    private Instant settledAt;
    private final Instant createdAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Settlement(Long orderId, Long orderLineId, Long creatorId, Long grossAmount, Long fee, CreatorAccount creatorAccount) {
        validateCreate(orderId, orderLineId, creatorId, grossAmount, fee);
        this.id = null;
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.creatorId = creatorId;
        this.status = SettlementStatus.PENDING;
        this.grossAmount = grossAmount;
        this.fee = fee;
        this.netAmount = grossAmount - fee;
        this.creatorAccount = creatorAccount;
        this.settledAt = null;
        this.createdAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Settlement(SettlementId id, Long orderId, Long orderLineId, Long creatorId, SettlementStatus status,
                      Long grossAmount, Long fee, Long netAmount,
                      CreatorAccount creatorAccount, Instant settledAt, Instant createdAt) {
        validateReconstruct(id, orderId, orderLineId, status, grossAmount, fee, netAmount, createdAt);
        this.id = id;
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.creatorId = creatorId;
        this.status = status;
        this.grossAmount = grossAmount;
        this.fee = fee;
        this.netAmount = netAmount;
        this.creatorAccount = creatorAccount;
        this.settledAt = settledAt;
        this.createdAt = createdAt;
    }

    private void validateCreate(Long orderId, Long orderLineId, Long creatorId, Long grossAmount, Long fee) {
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (creatorId != null && creatorId <= 0) throw new InvalidFieldException("크리에이터 ID는 양수여야 합니다.");
        if (grossAmount == null || grossAmount < 0) throw new InvalidFieldException("총 판매 금액은 0 이상이어야 합니다.");
        if (fee == null || fee < 0) throw new InvalidFieldException("수수료는 0 이상이어야 합니다.");
        if (fee > grossAmount) throw new InvalidFieldException("수수료는 총 판매 금액보다 클 수 없습니다.");
    }

    private void validateReconstruct(SettlementId id, Long orderId, Long orderLineId, SettlementStatus status,
                                     Long grossAmount, Long fee, Long netAmount, Instant createdAt) {
        if (id == null) throw new InvalidFieldException("정산 ID는 필수입니다.");
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (status == null) throw new InvalidFieldException("정산 상태는 필수입니다.");
        if (grossAmount == null || grossAmount < 0) throw new InvalidFieldException("총 판매 금액은 0 이상이어야 합니다.");
        if (fee == null || fee < 0) throw new InvalidFieldException("수수료는 0 이상이어야 합니다.");
        if (netAmount == null || netAmount < 0) throw new InvalidFieldException("순 정산 금액은 0 이상이어야 합니다.");
        if (createdAt == null) throw new InvalidFieldException("생성 시각은 필수입니다.");
    }

    public void applyFinancials(Long creatorId, Long grossAmount, Long fee, CreatorAccount creatorAccount) {
        if (this.status == SettlementStatus.SETTLED || this.status == SettlementStatus.CANCELLED) {
            throw new InvalidFieldException("정산 완료 또는 취소 상태에서는 금액을 변경할 수 없습니다.");
        }
        if (grossAmount == null || grossAmount < 0) throw new InvalidFieldException("총 판매 금액은 0 이상이어야 합니다.");
        if (fee == null || fee < 0) throw new InvalidFieldException("수수료는 0 이상이어야 합니다.");
        if (fee > grossAmount) throw new InvalidFieldException("수수료는 총 판매 금액보다 클 수 없습니다.");
        this.creatorId = creatorId;
        this.grossAmount = grossAmount;
        this.fee = fee;
        this.netAmount = grossAmount - fee;
        this.creatorAccount = creatorAccount;
    }

    public void readyToSettle() {
        validateStatus(SettlementStatus.PENDING);
        this.status = SettlementStatus.READY_TO_SETTLE;
    }

    public void settle() {
        validateStatus(SettlementStatus.READY_TO_SETTLE);
        this.status = SettlementStatus.SETTLED;
        this.settledAt = Instant.now();
    }

    public void cancel() {
        if (this.status == SettlementStatus.SETTLED) {
            throw new InvalidFieldException("이미 정산 완료된 건은 취소할 수 없습니다.");
        }
        this.status = SettlementStatus.CANCELLED;
    }

    private void validateStatus(SettlementStatus expected) {
        if (this.status != expected) {
            throw new InvalidFieldException(
                    "현재 상태에서 해당 작업을 수행할 수 없습니다. 현재: " + this.status + ", 기대: " + expected);
        }
    }
}
