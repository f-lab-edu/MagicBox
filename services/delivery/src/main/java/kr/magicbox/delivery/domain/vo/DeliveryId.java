package kr.magicbox.delivery.domain.vo;

import kr.magicbox.delivery.domain.exception.InvalidFieldException;

public record DeliveryId(Long value) {

    public DeliveryId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("배송 ID는 양수여야 합니다.");
        }
    }

    public static DeliveryId of(Long value) {
        return new DeliveryId(value);
    }
}
