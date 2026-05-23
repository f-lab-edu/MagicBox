package kr.magicbox.payment.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment_outbox")
public class PaymentOutboxEntity extends BaseEntity {

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "JSON")
    private String payload;

    @Builder
    public PaymentOutboxEntity(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}
