package kr.magicbox.user.application.dto;

import kr.magicbox.user.domain.vo.Nickname;
import lombok.Builder;

@Builder
public record UpdateUserProfileCommand(
        Nickname nickname,
        String profile,
        Boolean isReviewVisible
) {
}