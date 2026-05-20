package kr.magicbox.auth.domain.aggregate;

import kr.magicbox.auth.domain.exception.ExpiredRefreshTokenException;
import kr.magicbox.auth.domain.exception.InvalidFieldException;
import kr.magicbox.auth.domain.exception.RevokedRefreshTokenException;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class RefreshToken {
    private final UserId userId;
    private RefreshTokenValue refreshTokenValue;
    private final Instant expiresAt;
    private final Instant createdAt;
    private boolean isRevoked;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public RefreshToken(RefreshTokenValue refreshTokenValue, UserId userId, Instant expiresAt) {
        validateFields(refreshTokenValue, userId, expiresAt);

        this.refreshTokenValue = refreshTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
        this.isRevoked = false;
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public RefreshToken(RefreshTokenValue refreshTokenValue, UserId userId, Instant expiresAt,
                        Instant createdAt, boolean isRevoked) {
        this.refreshTokenValue = refreshTokenValue;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.isRevoked = isRevoked;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !isRevoked && !isExpired();
    }

    public void validate() {
        if (isRevoked) {
            throw new RevokedRefreshTokenException();
        }
        if (isExpired()) {
            throw new ExpiredRefreshTokenException();
        }
    }

    public void updateToken(RefreshTokenValue refreshTokenValue) {
        validateRefreshTokenValue(refreshTokenValue);
        this.refreshTokenValue = refreshTokenValue;
    }

    public void revoke() {
        this.isRevoked = true;
    }

    private void validateFields(RefreshTokenValue refreshTokenValue, UserId userId, Instant expiresAt) {
        validateRefreshTokenValue(refreshTokenValue);

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

    private void validateRefreshTokenValue(RefreshTokenValue refreshTokenValue) {
        if (refreshTokenValue == null) {
            throw new InvalidFieldException("토큰은 필수 값입니다.");
        }
    }
}
