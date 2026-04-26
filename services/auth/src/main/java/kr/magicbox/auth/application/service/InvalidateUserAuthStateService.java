package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvalidateUserAuthStateService {
    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    public void invalidate(UserId userId) {
        refreshTokenRepositoryPort.deleteRefreshToken(userId);
    }
}