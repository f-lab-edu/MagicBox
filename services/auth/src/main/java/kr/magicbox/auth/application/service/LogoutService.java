package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.LogoutCommand;
import kr.magicbox.auth.application.port.in.LogoutUseCase;
import kr.magicbox.auth.application.port.out.LogoutEventPublisherPort;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;
    private final LogoutEventPublisherPort logoutEventPublisherPort;

    @Override
    public void logout(LogoutCommand command) {
        refreshTokenRepositoryPort.deleteRefreshToken(UserId.of(command.userId()));
        logoutEventPublisherPort.publish(new UserLoggedOutEvent(command.userId()));
    }
}