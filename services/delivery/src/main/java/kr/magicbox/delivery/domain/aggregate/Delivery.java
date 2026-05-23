package kr.magicbox.delivery.domain.aggregate;

import kr.magicbox.delivery.domain.enums.DeliveryStatus;
import kr.magicbox.delivery.domain.exception.InvalidFieldException;
import kr.magicbox.delivery.domain.vo.DeliveryId;
import kr.magicbox.delivery.domain.vo.TrackingInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Delivery {

    private final DeliveryId id;
    private final Long orderLineId;
    private final Long orderId;
    private DeliveryStatus status;
    private final TrackingInfo trackingInfo;
    private final List<TrackingHistory> trackingHistories;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Delivery(Long orderLineId, Long orderId, TrackingInfo trackingInfo) {
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (trackingInfo == null) throw new InvalidFieldException("운송 정보는 필수입니다.");
        this.id = null;
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.status = DeliveryStatus.SHIPPED;
        this.trackingInfo = trackingInfo;
        this.trackingHistories = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Delivery(DeliveryId id, Long orderLineId, Long orderId, DeliveryStatus status, TrackingInfo trackingInfo,
                    List<TrackingHistory> trackingHistories, Instant createdAt, Instant updatedAt) {
        if (id == null) throw new InvalidFieldException("배송 ID는 필수입니다.");
        if (orderLineId == null || orderLineId <= 0) throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        if (orderId == null || orderId <= 0) throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        if (status == null) throw new InvalidFieldException("배송 상태는 필수입니다.");
        if (trackingInfo == null) throw new InvalidFieldException("운송 정보는 필수입니다.");
        if (createdAt == null) throw new InvalidFieldException("생성 시각은 필수입니다.");
        if (updatedAt == null) throw new InvalidFieldException("수정 시각은 필수입니다.");
        this.id = id;
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.status = status;
        this.trackingInfo = trackingInfo;
        this.trackingHistories = trackingHistories != null ? new ArrayList<>(trackingHistories) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void transit(TrackingHistory history) {
        validateStatus(DeliveryStatus.SHIPPED);
        this.status = DeliveryStatus.IN_TRANSIT;
        addHistory(history);
        this.updatedAt = Instant.now();
    }

    public void complete(TrackingHistory history) {
        if (this.status != DeliveryStatus.SHIPPED && this.status != DeliveryStatus.IN_TRANSIT) {
            throw new InvalidFieldException("현재 상태에서 배송 완료 처리를 할 수 없습니다: " + this.status);
        }
        this.status = DeliveryStatus.DELIVERED;
        addHistory(history);
        this.updatedAt = Instant.now();
    }

    public void addHistory(TrackingHistory history) {
        if (history != null) {
            this.trackingHistories.add(history);
        }
    }

    private void validateStatus(DeliveryStatus expected) {
        if (this.status != expected) {
            throw new InvalidFieldException(
                    "현재 상태에서 해당 작업을 수행할 수 없습니다. 현재: " + this.status + ", 기대: " + expected);
        }
    }
}
