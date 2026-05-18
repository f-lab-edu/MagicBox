package kr.magicbox.review.domain.vo;

import kr.magicbox.review.domain.exception.InvalidFieldException;

public record ReviewId(Long value) {
    public ReviewId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("리뷰 ID는 양수여야 합니다.");
        }
    }

    public static ReviewId of(Long value) {
        return new ReviewId(value);
    }
}
