package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.vo.UserId;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    void saveRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> getRefreshToken(UserId userId);
    void deleteRefreshToken(UserId userId);
}