package kr.magicbox.auth.application.dto;

import lombok.Builder;

@Builder
public record IssueTokenResult(
        String accessToken,
        String refreshToken
) {
}
