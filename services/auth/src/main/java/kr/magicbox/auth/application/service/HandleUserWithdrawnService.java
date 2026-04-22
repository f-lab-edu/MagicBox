package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.in.HandleUserWithdrawnUseCase;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleUserWithdrawnService implements HandleUserWithdrawnUseCase {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Override
    public void handleUserWithdrawn(UserId userId) {
        refreshTokenRepositoryPort.deleteRefreshToken(userId);
    }
}
