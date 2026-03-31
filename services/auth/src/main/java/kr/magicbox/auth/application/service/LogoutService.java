package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.LogoutCommand;
import kr.magicbox.auth.application.port.in.LogoutUseCase;
import kr.magicbox.auth.application.port.out.AuthDomainEventRepositoryPort;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.application.port.out.UserStatusPort;
import kr.magicbox.auth.domain.event.AuthDomainEvent;
import kr.magicbox.auth.domain.event.AuthDomainEventType;
import kr.magicbox.auth.domain.exception.InActiveUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {
    private final UserStatusPort userStatusPort;
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final AuthDomainEventRepositoryPort authDomainEventRepositoryPort;

    @Override
    @Transactional
    public void logout(LogoutCommand command) {
        if (!userStatusPort.isActive(command.userId().value())) {
            throw new InActiveUserException();
        }
        refreshTokenRepositoryPort.deleteRefreshToken(command.userId());

        // OutBox Pattern Applies
        AuthDomainEvent loggedOutEvent = AuthDomainEvent.builder()
                .key(command.userId().value().toString())
                .eventType(AuthDomainEventType.USER_LOGGED_OUT)
                .payload(command)
                .build();
        authDomainEventRepositoryPort.save(loggedOutEvent);
    }
}
