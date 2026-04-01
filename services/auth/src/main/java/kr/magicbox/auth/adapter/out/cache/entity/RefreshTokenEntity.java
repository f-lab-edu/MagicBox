package kr.magicbox.auth.adapter.out.cache.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token")
public class RefreshTokenEntity {

    @Id
    private Long userId;

    private String token;

    private Instant expiresAt;

    private Instant createdAt;

    private boolean isRevoked;

    @Builder
    public RefreshTokenEntity(String token, Long userId, Instant expiresAt, Instant createdAt, boolean isRevoked) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.isRevoked = isRevoked;
    }
}
