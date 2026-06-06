package kr.magicbox.delivery.adapter.out.persistence.entity;

import jakarta.persistence.*;
import kr.magicbox.delivery.domain.enums.DeliveryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery")
public class DeliveryEntity extends BaseEntity {

    @Column(name = "order_line_id", nullable = false, unique = true)
    private Long orderLineId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryStatus status;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Version
    private Integer version;

    @Builder
    public DeliveryEntity(Long orderLineId, Long orderId, Long customerId, DeliveryStatus status, String carrierCode, String trackingNumber) {
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.status = status;
        this.carrierCode = carrierCode;
        this.trackingNumber = trackingNumber;
        this.isDeleted = false;
    }

    public void update(DeliveryStatus status, String carrierCode, String trackingNumber) {
        this.status = status;
        this.carrierCode = carrierCode;
        this.trackingNumber = trackingNumber;
    }
}
