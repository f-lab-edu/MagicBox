package kr.magicbox.review.application.dto.command;

public record DeleteReviewCommand(
        Long reviewId,
        Long userId
) {
}
