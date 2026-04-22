package kr.magicbox.subscribe.domain.vo;

import kr.magicbox.subscribe.domain.exception.InvalidFieldException;

public record SubscriptionId(Long value) {
    public SubscriptionId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("구독 ID는 양수여야 합니다.");
        }
    }

    public static SubscriptionId of(Long value) {
        return new SubscriptionId(value);
    }
}
