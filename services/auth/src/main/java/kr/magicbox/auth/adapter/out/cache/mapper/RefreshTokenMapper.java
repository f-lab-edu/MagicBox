package kr.magicbox.auth.adapter.out.cache.mapper;

import kr.magicbox.auth.adapter.out.cache.entity.RefreshTokenEntity;
import kr.magicbox.auth.domain.aggregate.RefreshToken;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .token(refreshToken.getRefreshTokenValue().refreshTokenValue())
                .userId(refreshToken.getUserId().value())
                .expiresAt(refreshToken.getExpiresAt())
                .createdAt(refreshToken.getCreatedAt())
                .isRevoked(refreshToken.isRevoked())
                .build();
    }

    public RefreshToken toDomain(RefreshTokenEntity entity) {
        return RefreshToken.reconstructBuilder()
                .refreshTokenValue(RefreshTokenValue.of(entity.getToken()))
                .userId(UserId.of(entity.getUserId()))
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .isRevoked(entity.isRevoked())
                .build();
    }
}
