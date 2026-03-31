package kr.magicbox.auth.adapter.out.cache.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Getter
@NoArgsConstructor
@RedisHash(value = "code")
public class CodeEntity {

    @Id
    private String code;

    private Long userId;

    private String role;

    private Instant expiresAt;

    private Instant createdAt;

    @Builder
    public CodeEntity(String code, Long userId, String role, Instant expiresAt, Instant createdAt) {
        this.code = code;
        this.userId = userId;
        this.role = role;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }
}
