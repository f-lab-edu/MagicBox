package kr.magicbox.review.application.port.in;

import kr.magicbox.review.application.dto.query.GetReviewsByTargetQuery;
import kr.magicbox.review.domain.aggregate.Review;

import java.util.List;

public interface GetReviewsByTargetUseCase {
    List<Review> getReviewsByTarget(GetReviewsByTargetQuery query);
}
