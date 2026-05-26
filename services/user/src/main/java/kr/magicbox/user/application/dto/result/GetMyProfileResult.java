package kr.magicbox.user.application.dto.result;

import kr.magicbox.user.domain.enums.UserRole;
import lombok.Builder;

@Builder
public record GetMyProfileResult(
        Long id,
        String nickname,
        String profile,
        UserRole role
) {
}
