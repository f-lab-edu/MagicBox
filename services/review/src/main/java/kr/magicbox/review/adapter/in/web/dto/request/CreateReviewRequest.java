package kr.magicbox.review.adapter.in.web.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.magicbox.review.application.dto.command.CreateReviewCommand;
import kr.magicbox.review.domain.enums.ReviewTargetType;

public record CreateReviewRequest(
        @NotNull(message = "크리에이터 ID는 필수입니다.") @Positive(message = "크리에이터 ID는 양수여야 합니다.") Long creatorId,
        @NotNull(message = "대상 ID는 필수입니다.") @Positive(message = "대상 ID는 양수여야 합니다.") Long targetId,
        @NotNull(message = "대상 타입은 필수입니다.") ReviewTargetType targetType,
        @NotNull(message = "주문 ID는 필수입니다.") @Positive(message = "주문 ID는 양수여야 합니다.") Long orderId,
        @NotNull(message = "평점은 필수입니다.") @Min(value = 1, message = "평점은 1 이상이어야 합니다.") @Max(value = 5, message = "평점은 5 이하여야 합니다.") Integer rating,
        @NotBlank(message = "내용은 필수입니다.") String content
) {
    public CreateReviewCommand toCommand(Long userId) {
        return new CreateReviewCommand(userId, creatorId, targetId, targetType, orderId, rating, content);
    }
}
