package kr.magicbox.settlement.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kr.magicbox.settlement.domain.enums.SettlementStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "settlements")
public class SettlementEntity extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_line_id", nullable = false, unique = true)
    private Long orderLineId;

    @Column(name = "creator_id")
    private Long creatorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status;

    @Column(name = "gross_amount", nullable = false)
    private Long grossAmount;

    @Column(nullable = false)
    private Long fee;

    @Column(name = "net_amount", nullable = false)
    private Long netAmount;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "settled_at")
    private Instant settledAt;

    @Builder
    public SettlementEntity(Long orderId, Long orderLineId, Long creatorId, SettlementStatus status,
                            Long grossAmount, Long fee, Long netAmount,
                            String bankCode, String accountNumber, String accountHolder, Instant settledAt) {
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.creatorId = creatorId;
        this.status = status;
        this.grossAmount = grossAmount;
        this.fee = fee;
        this.netAmount = netAmount;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.settledAt = settledAt;
    }

    public void update(SettlementStatus status, Long creatorId, Long grossAmount, Long fee, Long netAmount,
                       String bankCode, String accountNumber, String accountHolder, Instant settledAt) {
        this.status = status;
        this.creatorId = creatorId;
        this.grossAmount = grossAmount;
        this.fee = fee;
        this.netAmount = netAmount;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.settledAt = settledAt;
    }
}
