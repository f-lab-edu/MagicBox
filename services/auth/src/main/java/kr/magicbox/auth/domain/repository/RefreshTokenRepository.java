package kr.magicbox.auth.domain.repository;

import kr.magicbox.auth.domain.aggregate.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    void saveRefreshToken(RefreshToken refreshToken);
    Optional<RefreshToken> getRefreshToken(String token);
    void deleteRefreshToken(String token);
}