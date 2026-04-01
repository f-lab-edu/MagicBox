package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;

public interface TokenManager {
    AccessTokenValue generateAccessToken(UserId userId, UserRole role);
    RefreshTokenValue generateRefreshToken(UserId userId, UserRole role);
    UserId extractUserId(String token);
    UserRole extractRole(String token);
    long getRefreshTokenExpiration();
}
