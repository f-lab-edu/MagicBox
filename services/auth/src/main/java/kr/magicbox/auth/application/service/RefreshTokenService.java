package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.out.cache.exception.RefreshTokenNotFoundException;
import kr.magicbox.auth.application.dto.command.RefreshTokenCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;
import kr.magicbox.auth.application.port.in.RefreshTokenUseCase;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.application.port.out.TokenManager;
import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final TokenManager tokenManager;

    @Override
    public TokenResult refresh(RefreshTokenCommand command) {
        String refreshToken = command.refreshToken();
        UserId userId = tokenManager.extractUserId(refreshToken);
        UserRole userRole = tokenManager.extractRole(refreshToken);

        validateStoredRefreshToken(userId);

        AccessTokenValue accessTokenValue = tokenManager.generateAccessToken(userId, userRole);
        RefreshTokenValue refreshTokenValue = tokenManager.generateRefreshToken(userId, userRole);

        rotateRefreshToken(userId, refreshTokenValue);

        return TokenResult.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .build();
    }

    private void validateStoredRefreshToken(UserId userId) {
        RefreshToken token = refreshTokenRepositoryPort.getRefreshToken(userId)
                .orElseThrow(RefreshTokenNotFoundException::new);
        token.validate();
    }

    private void rotateRefreshToken(UserId userId, RefreshTokenValue refreshTokenValue) {
        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        RefreshToken newRefreshToken = RefreshToken.createBuilder()
                .refreshTokenValue(refreshTokenValue)
                .userId(userId)
                .expiresAt(expiresAt)
                .build();
        refreshTokenRepositoryPort.rotateRefreshToken(newRefreshToken);
    }
}
