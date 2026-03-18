package kr.magicbox.auth.domain.aggregate;

import kr.magicbox.auth.domain.exception.InvalidFieldException;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RefreshToken {
    private final String token;
    private final UserId userId;
    private final Instant expiresAt;
    private final Instant createdAt;
    private boolean isRevoked;

    @Builder
    public RefreshToken(String token, UserId userId, Instant expiresAt) {
        validateFields(token, userId, expiresAt);
        
        this.token = token;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
        this.isRevoked = false;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !isRevoked && !isExpired();
    }

    public void revoke() {
        this.isRevoked = true;
    }

    private void validateFields(String token, UserId userId, Instant expiresAt) {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidFieldException("토큰은 필수 값입니다.");
        }
        
        if (userId == null) {
            throw new InvalidFieldException("사용자 ID는 필수 값입니다.");
        }
        
        if (expiresAt == null) {
            throw new InvalidFieldException("만료 시간은 필수 값입니다.");
        }
        
        if (expiresAt.isBefore(Instant.now())) {
            throw new InvalidFieldException("만료 시간은 현재 시간 이후여야 합니다.");
        }
    }
}