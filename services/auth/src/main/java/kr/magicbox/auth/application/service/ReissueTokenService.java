package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.out.persistence.exception.RefreshTokenNotFoundException;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.ReissueTokenUseCase;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.application.port.out.TokenManager;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPortPort;
    private final TokenManager tokenManager;

    @Override
    public TokenResult reissueToken(String refreshToken) {
        // 1. JWT 에서 UserId와 UserRole 추출
        UserId userId = tokenManager.extractUserId(refreshToken);
        UserRole userRole = tokenManager.extractRole(refreshToken);

        // 2. RefreshToken 검증 및 조회
        RefreshToken token = refreshTokenRepositoryPort.getRefreshToken(userId)
                .orElseThrow(RefreshTokenNotFoundException::new);

        token.validate();

        // 3. 새로운 토큰 생성
        TokenResult tokenResult = tokenManager.generateTokenPair(userId, userRole);

        // 4. 새로운 RefreshToken 저장 (덮어쓰기)
        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(tokenResult.refreshToken())
                .userId(userId)
                .expiresAt(expiresAt)
                .build();

        refreshTokenRepositoryPort.saveRefreshToken(newRefreshToken);

        // 5. 결과 반환
        return TokenResult.builder()
                .accessToken(tokenResult.accessToken())
                .refreshToken(tokenResult.refreshToken())
                .build();
    }
}