package kr.magicbox.review.application.dto.query;

import kr.magicbox.review.domain.vo.CreatorId;

public record GetReviewRatingQuery(CreatorId creatorId) {
    public static GetReviewRatingQuery of(CreatorId creatorId) {
        return new GetReviewRatingQuery(creatorId);
    }
}
