package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.query.GetReviewRatingQuery;

public interface GetReviewRatingUseCase {
    double getReviewRating(GetReviewRatingQuery query);
}
