package kr.magicbox.review.application.dto.query;

import kr.magicbox.review.domain.enums.ReviewTargetType;

public record GetReviewsByTargetQuery(Long targetId, ReviewTargetType targetType) {
    public static GetReviewsByTargetQuery of(Long targetId, ReviewTargetType targetType) {
        return new GetReviewsByTargetQuery(targetId, targetType);
    }
}
