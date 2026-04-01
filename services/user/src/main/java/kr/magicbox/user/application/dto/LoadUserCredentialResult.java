package kr.magicbox.user.application.dto;

import kr.magicbox.user.domain.enums.UserRole;
import kr.magicbox.user.domain.vo.UserId;
import lombok.Builder;

@Builder
public record LoadUserCredentialResult(UserId userId, UserRole userRole) {
}
