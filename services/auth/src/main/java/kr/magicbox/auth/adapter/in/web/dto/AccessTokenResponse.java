package kr.magicbox.auth.adapter.in.web.dto;

import lombok.Builder;

@Builder
public record AccessTokenResponse(String accessToken) {
}
