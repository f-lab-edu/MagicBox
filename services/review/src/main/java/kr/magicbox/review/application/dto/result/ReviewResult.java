package kr.magicbox.review.application.dto.result;

import kr.magicbox.review.domain.aggregate.Review;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReviewResult(
        Long reviewId,
        Long userId,
        Long creatorId,
        Long targetId,
        ReviewTargetType targetType,
        Long orderId,
        Integer rating,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
    public static ReviewResult from(Review review) {
        return ReviewResult.builder()
                .reviewId(review.getId().value())
                .userId(review.getUserId().value())
                .creatorId(review.getCreatorId().value())
                .targetId(review.getTargetId())
                .targetType(review.getTargetType())
                .orderId(review.getOrderId().value())
                .rating(review.getRating())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
