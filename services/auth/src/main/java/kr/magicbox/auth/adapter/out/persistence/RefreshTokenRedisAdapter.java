package kr.magicbox.auth.adapter.out.persistence;

import kr.magicbox.auth.adapter.out.persistence.entity.RefreshTokenEntity;
import kr.magicbox.auth.adapter.out.persistence.mapper.RefreshTokenMapper;
import kr.magicbox.auth.adapter.out.persistence.repository.RefreshTokenRedisRepository;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisAdapter implements RefreshTokenRepository {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRedisRepository.save(entity);
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String token) {
        return refreshTokenRedisRepository.findById(token)
                .map(refreshTokenMapper::toDomain);
    }

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRedisRepository.deleteById(token);
    }
}