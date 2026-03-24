package kr.magicbox.auth.domain.vo;

import kr.magicbox.auth.domain.exception.InvalidFieldException;

public record AccessTokenValue(String accessTokenValue) {

    public AccessTokenValue {
        if (accessTokenValue == null || accessTokenValue.trim().isEmpty()) {
            throw new InvalidFieldException("액세스 토큰은 필수 값입니다.");
        }
    }

    public static AccessTokenValue of(String accessTokenValue) {
        return new AccessTokenValue(accessTokenValue);
    }
}