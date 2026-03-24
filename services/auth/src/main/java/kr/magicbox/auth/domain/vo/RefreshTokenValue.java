package kr.magicbox.auth.domain.vo;

import kr.magicbox.auth.domain.exception.InvalidFieldException;

public record RefreshTokenValue(String refreshTokenValue) {

    public RefreshTokenValue {
        if (refreshTokenValue == null || refreshTokenValue.trim().isEmpty()) {
            throw new InvalidFieldException("리프레시 토큰은 필수 값입니다.");
        }
    }

    public static RefreshTokenValue of(String refreshTokenValue) {
        return new RefreshTokenValue(refreshTokenValue);
    }
}