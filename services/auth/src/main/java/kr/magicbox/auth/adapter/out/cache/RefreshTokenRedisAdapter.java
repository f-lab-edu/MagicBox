package kr.magicbox.auth.adapter.out.cache;

import kr.magicbox.auth.adapter.out.cache.entity.RefreshTokenEntity;
import kr.magicbox.auth.adapter.out.cache.mapper.RefreshTokenMapper;
import kr.magicbox.auth.adapter.out.cache.repository.RefreshTokenRedisRepository;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.application.port.out.RefreshTokenRepositoryPort;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisAdapter implements RefreshTokenRepositoryPort {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public void save(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRedisRepository.save(entity);
    }

    @Override
    public void rotateRefreshToken(RefreshToken refreshToken) {
        RefreshTokenEntity entity = refreshTokenMapper.toEntity(refreshToken);
        refreshTokenRedisRepository.save(entity);
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(UserId userId) {
        return refreshTokenRedisRepository.findById(userId.value())
                .map(refreshTokenMapper::toDomain);
    }

    @Override
    public void deleteRefreshToken(UserId userId) {
        refreshTokenRedisRepository.deleteById(userId.value());
    }
}
