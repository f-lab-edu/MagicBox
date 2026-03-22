package kr.magicbox.user.application.dto;

import lombok.Builder;

@Builder
public record UpdateUserProfileCommand(
        String nickname,
        String profile,
        Boolean isReviewVisible
) {
}