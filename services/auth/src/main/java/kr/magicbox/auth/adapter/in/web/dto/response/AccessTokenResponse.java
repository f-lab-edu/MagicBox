package kr.magicbox.auth.adapter.in.web.dto.response;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {
}
