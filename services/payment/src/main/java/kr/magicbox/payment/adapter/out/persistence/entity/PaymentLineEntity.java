package kr.magicbox.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment_lines")
public class PaymentLineEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private PaymentEntity payment;

    @Column(name = "order_line_id", nullable = false)
    private Long orderLineId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Builder
    public PaymentLineEntity(PaymentEntity payment, Long orderLineId, Long sellerId, Long amount) {
        this.payment = payment;
        this.orderLineId = orderLineId;
        this.sellerId = sellerId;
        this.amount = amount;
    }
}
