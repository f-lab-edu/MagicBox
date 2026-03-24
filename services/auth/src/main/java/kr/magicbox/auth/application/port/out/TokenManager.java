package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;

public interface TokenManager {
    TokenResult generateTokenPair(UserId userId, UserRole role);
    UserId extractUserId(String token);
    UserRole extractRole(String token);
    long getRefreshTokenExpiration();
}