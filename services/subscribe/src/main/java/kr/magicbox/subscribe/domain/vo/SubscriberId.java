package kr.magicbox.subscribe.domain.vo;

import kr.magicbox.subscribe.domain.exception.InvalidFieldException;

public record SubscriberId(Long value) {
    public SubscriberId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("구독자 ID는 양수여야 합니다.");
        }
    }

    public static SubscriberId of(Long value) {
        return new SubscriberId(value);
    }
}
