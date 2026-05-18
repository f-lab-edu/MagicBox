package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.query.GetReviewsByUserIdQuery;
import kr.magicbox.review.domain.aggregate.Review;

import java.util.List;

public interface GetReviewsByUserIdUseCase {
    List<Review> getReviewsByUserId(GetReviewsByUserIdQuery query);
}
