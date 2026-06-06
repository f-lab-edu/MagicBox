package kr.magicbox.media.domain.vo;

import kr.magicbox.media.domain.exception.InvalidFieldException;

public record MediaId(Long value) {

    public MediaId {
        if (value == null || value <= 0) throw new InvalidFieldException("미디어 ID는 양수여야 합니다.");
    }

    public static MediaId of(Long value) {
        return new MediaId(value);
    }
}
