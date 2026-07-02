package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.command.EmailLoginCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;
import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.application.port.in.EmailLoginUseCase;
import kr.magicbox.auth.application.port.out.AuthOutboxPort;
import kr.magicbox.auth.application.port.out.EmailUserPort;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.application.port.out.TokenManager;
import kr.magicbox.auth.application.port.out.UserStatusPort;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
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
public class EmailLoginService implements EmailLoginUseCase {

    private final EmailUserPort emailUserPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthOutboxPort authOutboxPort;
    private final UserStatusPort userStatusPort;
    private final TokenManager tokenManager;

    @Override
    @Transactional
    public TokenResult emailLogin(EmailLoginCommand command) {
        UserResult userResult = emailUserPort.verifyEmailCredential(
                command.email(), command.password()
        ).join();

        UserId userId = userResult.userId();
        AccessTokenValue accessTokenValue = tokenManager.generateAccessToken(userId, userResult.userRole());
        RefreshTokenValue refreshTokenValue = tokenManager.generateRefreshToken(userId, userResult.userRole());

        Instant expiresAt = Instant.now().plusMillis(tokenManager.getRefreshTokenExpiration());
        refreshTokenRepositoryPort.save(RefreshToken.createBuilder()
                .refreshTokenValue(refreshTokenValue)
                .userId(userId)
                .expiresAt(expiresAt)
                .build());

        Instant now = Instant.now();
        boolean isDuplicate = userStatusPort.isActive(userId.value()).join();
        AuthDomainEvent event = isDuplicate
                ? DuplicateLoginEvent.builder().userId(userId).occurredAt(now).build()
                : LoginEvent.builder().userId(userId).occurredAt(now).build();
        authOutboxPort.save(event);

        return TokenResult.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .build();
    }
}
