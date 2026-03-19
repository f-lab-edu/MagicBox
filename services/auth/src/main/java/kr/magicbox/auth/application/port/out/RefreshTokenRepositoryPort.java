package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.domain.aggregate.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepositoryPort {
    void saveRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> getRefreshToken(String token);
    void deleteRefreshToken(String token);
}