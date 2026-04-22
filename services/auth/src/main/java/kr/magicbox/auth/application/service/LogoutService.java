package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.in.LogoutUseCase;
import kr.magicbox.auth.application.port.out.*;
import kr.magicbox.auth.domain.event.LogoutEvent;
import kr.magicbox.auth.domain.exception.InActiveUserException;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {
    private final UserStatusPort userStatusPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthOutboxPort authOutboxPort;

    @Override
    @Transactional
    public void logout(UserId userId) {

        if (!userStatusPort.isActive(userId.value())) {
            throw new InActiveUserException();
        }
        refreshTokenRepositoryPort.deleteRefreshToken(userId);

        // OutBox Pattern Applies
        LogoutEvent loggedOutEvent = LogoutEvent.builder()
                .userId(userId)
                .createdAt(Instant.now())
                .build();
        authOutboxPort.save(loggedOutEvent);
    }
}
