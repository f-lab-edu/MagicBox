package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.command.LogoutCommand;
import kr.magicbox.auth.application.port.in.LogoutUseCase;
import kr.magicbox.auth.application.port.out.*;
import kr.magicbox.auth.domain.event.LogoutEvent;
import kr.magicbox.auth.domain.exception.UserInactiveException;
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
    public void logout(LogoutCommand command) {
        UserId userId = command.userId();

        if (!userStatusPort.isActive(userId.value()).join()) {
            throw new UserInactiveException();
        }
        refreshTokenRepositoryPort.deleteRefreshToken(userId);

        // OutBox Pattern Applies
        LogoutEvent loggedOutEvent = LogoutEvent.builder()
                .userId(userId)
                .occurredAt(Instant.now())
                .build();
        authOutboxPort.save(loggedOutEvent);
    }
}
