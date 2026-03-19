package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.exception.CodeNotFoundException;
import kr.magicbox.auth.application.dto.IssueTokenCommand;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.IssueTokenUseCase;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.repository.CodeRepository;
import kr.magicbox.auth.domain.repository.RefreshTokenRepository;
import kr.magicbox.auth.domain.service.TokenManager;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IssueTokenService implements IssueTokenUseCase {
    private final CodeRepository codeRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public TokenResult issueToken(IssueTokenCommand command) {
        // 1. Code 검증 및 조회
        Code code = codeRepository.getCodeByValue(command.code())
                .orElseThrow(CodeNotFoundException::new);

        code.validate();

        // 2. UserId와 UserRole 추출
        UserId userId = code.getUserId();
        UserRole userRole = code.getRole();

        // 3. 토큰 생성
        String accessToken = tokenManager.generateAccessToken(userId, userRole);
        String refreshTokenValue = tokenManager.generateRefreshToken(userId, userRole);

        // 4. RefreshToken 저장
        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .userId(userId)
                .expiresAt(expiresAt)
                .build();

        refreshTokenRepository.saveRefreshToken(refreshToken);

        // 5. Code 삭제 (일회용)
        codeRepository.deleteCode(command.code());

        // 6. 결과 반환
        return TokenResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .build();
    }
}