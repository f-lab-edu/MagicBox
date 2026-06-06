package kr.magicbox.delivery.domain.aggregate;

import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import kr.magicbox.delivery.domain.exception.InvalidFieldException;
import kr.magicbox.delivery.domain.vo.TrackingHistoryId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class TrackingHistory {

    private final TrackingHistoryId id;
    private final TrackingHistoryStatus status;
    private final String description;
    private final Instant trackedAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public TrackingHistory(TrackingHistoryStatus status, String description, Instant trackedAt) {
        if (status == null) throw new InvalidFieldException("추적 상태는 필수 값입니다.");
        this.id = null;
        this.status = status;
        this.description = description;
        this.trackedAt = trackedAt != null ? trackedAt : Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public TrackingHistory(TrackingHistoryId id, TrackingHistoryStatus status, String description, Instant trackedAt) {
        if (id == null) throw new InvalidFieldException("추적 이력 ID는 필수입니다.");
        if (status == null) throw new InvalidFieldException("추적 상태는 필수 값입니다.");
        if (trackedAt == null) throw new InvalidFieldException("추적 시각은 필수입니다.");
        this.id = id;
        this.status = status;
        this.description = description;
        this.trackedAt = trackedAt;
    }
}
