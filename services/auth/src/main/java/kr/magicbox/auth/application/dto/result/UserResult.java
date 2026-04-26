package kr.magicbox.auth.application.dto.result;

import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;

public record UserResult(UserId userId, UserRole userRole) {
}
