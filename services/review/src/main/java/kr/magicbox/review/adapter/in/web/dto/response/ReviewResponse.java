package kr.magicbox.review.adapter.in.web.dto.response;

import kr.magicbox.review.application.dto.result.ReviewResult;
import kr.magicbox.review.domain.enums.ReviewTargetType;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ReviewResponse(
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
    public static ReviewResponse from(ReviewResult result) {
        return ReviewResponse.builder()
                .reviewId(result.reviewId())
                .userId(result.userId())
                .creatorId(result.creatorId())
                .targetId(result.targetId())
                .targetType(result.targetType())
                .orderId(result.orderId())
                .rating(result.rating())
                .content(result.content())
                .createdAt(result.createdAt())
                .updatedAt(result.updatedAt())
                .build();
    }
}
