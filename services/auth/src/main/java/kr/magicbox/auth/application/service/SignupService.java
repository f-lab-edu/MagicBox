package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.command.SignupCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;
import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.application.port.in.SignupUseCase;
import kr.magicbox.auth.application.port.out.AuthOutboxPort;
import kr.magicbox.auth.application.port.out.EmailUserPort;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.application.port.out.TokenManager;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.event.LoginEvent;
import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SignupService implements SignupUseCase {

    private final EmailUserPort emailUserPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthOutboxPort authOutboxPort;
    private final TokenManager tokenManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public TokenResult signup(SignupCommand command) {
        String passwordHash = passwordEncoder.encode(command.password());

        UserResult userResult = emailUserPort.signupWithEmail(
                command.email(), passwordHash, command.nickname()
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

        authOutboxPort.save(LoginEvent.builder()
                .userId(userId)
                .occurredAt(Instant.now())
                .build());

        return TokenResult.builder()
                .accessToken(accessTokenValue)
                .refreshToken(refreshTokenValue)
                .build();
    }
}
