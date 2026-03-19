package kr.magicbox.auth.adapter.out.persistence.entity;

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
    private String token;

    private Long userId;

    private Instant expiresAt;

    private Instant createdAt;

    private boolean isRevoked;

    @Builder
    public RefreshTokenEntity(String token, Long userId, Instant expiresAt, Instant createdAt, boolean isRevoked) {
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.isRevoked = isRevoked;
    }
}
