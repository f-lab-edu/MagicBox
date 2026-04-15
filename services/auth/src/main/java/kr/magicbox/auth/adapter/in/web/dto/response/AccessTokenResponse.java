package kr.magicbox.auth.adapter.in.web.dto.response;

import kr.magicbox.auth.application.dto.result.TokenResult;
import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {

    public static AccessTokenResponse from(TokenResult result) {
        return AccessTokenResponse.builder()
                .accessToken(result.accessToken().accessTokenValue())
                .build();
    }
}