package kr.magicbox.auth.domain.aggregate;

import kr.magicbox.auth.domain.exception.InvalidFieldException;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Code {
    private final String code;
    private final String email;
    private final Instant expiresAt;
    private final Instant createdAt;

    @Builder
    public Code(String code, String email, Instant expiresAt) {
        validateFields(code, email, expiresAt);
        
        this.code = code;
        this.email = email;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public boolean isValid() {
        return !isExpired();
    }

    private void validateFields(String code, String email, Instant expiresAt) {
        if (code == null || code.trim().isEmpty()) {
            throw new InvalidFieldException("코드는 필수 값입니다.");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidFieldException("이메일은 필수 값입니다.");
        }
        
        if (expiresAt == null) {
            throw new InvalidFieldException("만료 시간은 필수 값입니다.");
        }
        
        if (expiresAt.isBefore(Instant.now())) {
            throw new InvalidFieldException("만료 시간은 현재 시간 이후여야 합니다.");
        }
    }
}