package kr.magicbox.delivery.domain.vo;

import kr.magicbox.delivery.domain.exception.InvalidFieldException;

public record TrackingHistoryId(Long value) {

    public TrackingHistoryId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("추적 이력 ID는 양수여야 합니다.");
        }
    }

    public static TrackingHistoryId of(Long value) {
        return new TrackingHistoryId(value);
    }
}
