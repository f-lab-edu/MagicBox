package kr.magicbox.review.adapter.in.web.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.review.application.dto.command.UpdateReviewCommand;

public record UpdateReviewRequest(
        @NotNull(message = "평점은 필수입니다.") @Min(value = 1, message = "평점은 1 이상이어야 합니다.") @Max(value = 5, message = "평점은 5 이하여야 합니다.") Integer rating,
        @NotBlank(message = "내용은 필수입니다.") String content
) {
    public UpdateReviewCommand toCommand(Long reviewId, Long userId) {
        return new UpdateReviewCommand(reviewId, userId, rating, content);
    }
}
