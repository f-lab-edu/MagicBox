package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.exception.RefreshTokenNotFoundException;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.ReissueTokenUseCase;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.repository.RefreshTokenRepository;
import kr.magicbox.auth.domain.service.TokenManager;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueTokenService implements ReissueTokenUseCase {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public TokenResult reissueToken(String refreshToken) {
        // 1. RefreshToken 검증 및 조회
        RefreshToken token = refreshTokenRepository.getRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNotFoundException::new);

        token.validate();

        // 2. JWT에서 UserId와 UserRole 추출
        UserId userId = tokenManager.extractUserId(refreshToken);
        UserRole userRole = tokenManager.extractRole(refreshToken);

        // 3. 새로운 토큰 생성
        String newAccessToken = tokenManager.generateAccessToken(userId, userRole);
        String newRefreshTokenValue = tokenManager.generateRefreshToken(userId, userRole);

        // 4. 기존 RefreshToken 삭제
        refreshTokenRepository.deleteRefreshToken(refreshToken);

        // 5. 새로운 RefreshToken 저장
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(newRefreshTokenValue)
                .userId(userId)
                .build();

        refreshTokenRepository.saveRefreshToken(newRefreshToken);

        // 6. 결과 반환
        return TokenResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenValue)
                .build();
    }
}