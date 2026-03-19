package kr.magicbox.auth.application.dto;

import lombok.Builder;

@Builder
public record TokenResult(
    String accessToken,
    String refreshToken
) {
}