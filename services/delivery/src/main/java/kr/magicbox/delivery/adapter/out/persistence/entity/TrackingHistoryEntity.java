package kr.magicbox.delivery.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delivery_tracking_history")
public class TrackingHistoryEntity extends BaseEntity {

    @Column(name = "delivery_id", nullable = false)
    private Long deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TrackingHistoryStatus status;

    @Column(name = "description")
    private String description;

    @Column(name = "tracked_at", nullable = false)
    private Instant trackedAt;

    @Builder
    public TrackingHistoryEntity(Long deliveryId, TrackingHistoryStatus status, String description, Instant trackedAt) {
        this.deliveryId = deliveryId;
        this.status = status;
        this.description = description;
        this.trackedAt = trackedAt;
    }
}
