package kr.magicbox.review.application.dto.query;

import kr.magicbox.review.domain.vo.UserId;

public record GetReviewsByUserIdQuery(UserId userId) {
    public static GetReviewsByUserIdQuery of(UserId userId) {
        return new GetReviewsByUserIdQuery(userId);
    }
}
