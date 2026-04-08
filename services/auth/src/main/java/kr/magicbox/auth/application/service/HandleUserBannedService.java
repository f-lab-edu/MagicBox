package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.dto.command.HandleUserBannedCommand;
import kr.magicbox.auth.application.port.in.HandleUserBannedUseCase;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleUserBannedService implements HandleUserBannedUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Override
    public void handleUserBanned(HandleUserBannedCommand command) {
        if (refreshTokenRepositoryPort.getRefreshToken(command.userId()).isEmpty()) {
            log.debug("이미 리프레시 토큰이 없는 사용자입니다. userId={}", command.userId().value());
            return;
        }
        refreshTokenRepositoryPort.deleteRefreshToken(command.userId());
    }
}
