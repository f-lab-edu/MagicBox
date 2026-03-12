package kr.magicbox.user.application.dto;

import lombok.Builder;
import java.time.Instant;

@Builder
public record UserReviewDto(
        Long reviewId,
        String content,
        Instant createdAt
) {
}