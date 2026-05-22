package kr.magicbox.auth.domain.aggregate;

import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.exception.ExpiredCodeException;
import kr.magicbox.auth.domain.exception.InvalidFieldException;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Code {
    private final String code;
    private final UserId userId;
    private final UserRole role;
    private final Instant expiresAt;
    private final Instant createdAt;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Code(String code, UserId userId, UserRole role, Instant expiresAt) {
        validateFields(code, userId, role, expiresAt);

        this.code = code;
        this.userId = userId;
        this.role = role;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Code(String code, UserId userId, UserRole role, Instant expiresAt, Instant createdAt) {
        this.code = code;
        this.userId = userId;
        this.role = role;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !isExpired();
    }

    public void validate() {
        if (isExpired()) {
            throw new ExpiredCodeException();
        }
    }

    private void validateFields(String code, UserId userId, UserRole role, Instant expiresAt) {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidFieldException("코드는 필수 값입니다.");
        }

        if (userId == null) {
            throw new InvalidFieldException("사용자 ID는 필수 값입니다.");
        }

        if (role == null) {
            throw new InvalidFieldException("사용자 역할은 필수 값입니다.");
        }

        if (expiresAt == null) {
            throw new InvalidFieldException("만료 시간은 필수 값입니다.");
        }

        if (expiresAt.isBefore(Instant.now())) {
            throw new InvalidFieldException("만료 시간은 현재 시간 이후여야 합니다.");
        }
    }
}
