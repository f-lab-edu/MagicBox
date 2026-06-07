package kr.magicbox.settlement.domain.vo;

import kr.magicbox.settlement.domain.exception.InvalidFieldException;

public record SettlementId(Long value) {

    public SettlementId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("정산 ID는 양수여야 합니다.");
        }
    }

    public static SettlementId of(Long value) {
        return new SettlementId(value);
    }
}
