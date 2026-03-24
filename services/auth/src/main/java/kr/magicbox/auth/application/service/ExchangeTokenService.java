package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.out.persistence.exception.CodeNotFoundException;
import kr.magicbox.auth.application.dto.ExchangeTokenCommand;
import kr.magicbox.auth.application.port.in.ExchangeTokenUseCase;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.application.port.out.CodeRepositoryPort;
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
public class ExchangeTokenService implements ExchangeTokenUseCase {
    private final CodeRepositoryPort codeRepositoryPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final TokenManager tokenManager;

    @Override
    public TokenResult exchange(ExchangeTokenCommand command) {
        Code code = validateAndGetCode(command.code());

        UserId userId = code.getUserId();
        UserRole userRole = code.getRole();

        AccessTokenValue accessTokenValue = tokenManager.generateAccessToken(userId, userRole);
        RefreshTokenValue refreshTokenValue = tokenManager.generateRefreshToken(userId, userRole);

        saveRefreshToken(userId, refreshTokenValue);
        deleteCode(code);

        return TokenResult.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .build();
    }

    private Code validateAndGetCode(String codeValue) {
        Code code = codeRepositoryPort.getCodeByValue(codeValue)
                .orElseThrow(CodeNotFoundException::new);
        code.validate();
        return code;
    }

    private void saveRefreshToken(UserId userId, RefreshTokenValue refreshTokenValue) {
        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshTokenValue(refreshTokenValue)
                .userId(userId)
                .expiresAt(expiresAt)
                .build();
        refreshTokenRepositoryPort.save(refreshToken);
    }

    private void deleteCode(Code code) {
        codeRepositoryPort.deleteById(code.getCode());
    }
}