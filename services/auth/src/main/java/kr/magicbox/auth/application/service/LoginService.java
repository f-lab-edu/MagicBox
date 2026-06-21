package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.out.cache.exception.CodeNotFoundException;
import kr.magicbox.auth.application.dto.command.LoginCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;
import kr.magicbox.auth.application.port.in.LoginUseCase;
import kr.magicbox.auth.application.port.out.*;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.event.AuthDomainEvent;
import kr.magicbox.auth.domain.event.DuplicateLoginEvent;
import kr.magicbox.auth.domain.event.LoginEvent;
import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final CodeRepositoryPort codeRepositoryPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthOutboxPort authOutboxPort;
    private final UserStatusPort userStatusPort;
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public TokenResult login(LoginCommand command) {
        Code code = validateAndGetCode(command.code());

        UserId userId = code.getUserId();
        UserRole userRole = code.getRole();

        AccessTokenValue accessTokenValue = tokenManager.generateAccessToken(userId, userRole);
        RefreshTokenValue refreshTokenValue = tokenManager.generateRefreshToken(userId, userRole);

        saveRefreshToken(userId, refreshTokenValue);
        deleteCode(code);
        saveLoginEvent(userId);

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

    private void saveLoginEvent(UserId userId) {
        Instant now = Instant.now();
        boolean isDuplicate = userStatusPort.isActive(userId.value()).join();

        AuthDomainEvent event = isDuplicate
                ? DuplicateLoginEvent.builder().userId(userId).occurredAt(now).build()
                : LoginEvent.builder().userId(userId).occurredAt(now).build();
        authOutboxPort.save(event);
    }

    private void saveRefreshToken(UserId userId, RefreshTokenValue refreshTokenValue) {
        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        RefreshToken refreshToken = RefreshToken.createBuilder()
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