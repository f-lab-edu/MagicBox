package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.in.HandleUserBannedUseCase;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleUserBannedService implements HandleUserBannedUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Override
    public void handleUserBanned(UserId userId) {
        refreshTokenRepositoryPort.deleteRefreshToken(userId);
    }
}