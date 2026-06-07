package kr.magicbox.review.application.dto.command;

import kr.magicbox.review.domain.enums.ReviewTargetType;

public record CreateReviewCommand(
        Long userId,
        Long creatorId,
        Long targetId,
        ReviewTargetType targetType,
        Long orderId,
        Integer rating,
        String content
) {
}
