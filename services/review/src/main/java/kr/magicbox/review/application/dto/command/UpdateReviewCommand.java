package kr.magicbox.review.application.dto.command;

public record UpdateReviewCommand(
        Long reviewId,
        Long userId,
        Integer rating,
        String content
) {
}
