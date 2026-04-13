package kr.magicbox.auth.application.dto.result;

import kr.magicbox.auth.domain.vo.AccessTokenValue;
import kr.magicbox.auth.domain.vo.RefreshTokenValue;
import lombok.Builder;

@Builder
public record TokenResult(
    AccessTokenValue accessToken,
    RefreshTokenValue refreshToken
) {
}