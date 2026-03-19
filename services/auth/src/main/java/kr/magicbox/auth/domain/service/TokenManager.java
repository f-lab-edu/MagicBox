package kr.magicbox.auth.domain.service;

import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;

public interface TokenManager {
    String generateAccessToken(UserId userId, UserRole role);
    String generateRefreshToken(UserId userId, UserRole role);
    UserId extractUserId(String token);
    UserRole extractRole(String token);
    long getRefreshTokenExpiration();
}