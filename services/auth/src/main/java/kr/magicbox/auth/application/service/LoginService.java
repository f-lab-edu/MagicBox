package kr.magicbox.auth.application.service;

import kr.magicbox.auth.adapter.out.cache.exception.CodeNotFoundException;
import kr.magicbox.auth.application.dto.LoginCommand;
import kr.magicbox.auth.application.port.in.LoginUseCase;
import kr.magicbox.auth.application.port.out.*;
import kr.magicbox.auth.domain.aggregate.Code;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.event.AuthDomainEvent;
import kr.magicbox.auth.domain.event.AuthDomainEventType;
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
    private final AuthDomainEventRepositoryPort authDomainEventRepositoryPort;
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

        validateDuplicateLogin(userId);

        return TokenResult.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .build();
    }

    private void validateDuplicateLogin(UserId userId) {
        AuthDomainEventType authDomainEventType = userStatusPort.isActive(userId.value()) ? AuthDomainEventType.USER_DUPLICATE_LOGGED_IN : AuthDomainEventType.USER_LOGGED_IN;

        // OutBox Pattern Applies
        AuthDomainEvent loggedInEvent = AuthDomainEvent.builder()
                .key(userId.value().toString())
                .eventType(authDomainEventType)
                .payload(userId)
                .build();
        authDomainEventRepositoryPort.save(loggedInEvent);
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
