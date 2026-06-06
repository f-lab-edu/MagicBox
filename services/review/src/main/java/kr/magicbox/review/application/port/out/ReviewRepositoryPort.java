package kr.magicbox.review.application.port.out;

import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import kr.magicbox.review.domain.vo.CreatorId;
import kr.magicbox.review.domain.vo.ReviewId;
import kr.magicbox.review.domain.vo.UserId;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryPort {
    void save(Review review);
    void update(Review review);
    Optional<Review> findById(ReviewId reviewId);
    List<Review> findAllByUserId(UserId userId);
    List<Review> findAllByTarget(Long targetId, ReviewTargetType targetType);
    double calculateAverageRatingByCreatorId(CreatorId creatorId);
    boolean existsByOrderId(Long orderId);
}
