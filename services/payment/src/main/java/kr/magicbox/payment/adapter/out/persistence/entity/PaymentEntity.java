package kr.magicbox.payment.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.payment.domain.enums.PaymentStatus;
import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payments")
public class PaymentEntity extends BaseEntity {

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "payment_key")
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private TossPaymentMethod paymentMethod;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentLineEntity> paymentLines = new ArrayList<>();

    @Version
    private Integer version;

    @Builder
    public PaymentEntity(Long orderId, Long customerId, PaymentStatus status,
                         Long totalAmount, String currency) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
    }

    public void update(PaymentStatus status, String paymentKey, TossPaymentMethod paymentMethod) {
        this.status = status;
        this.paymentKey = paymentKey;
        this.paymentMethod = paymentMethod;
    }
}
