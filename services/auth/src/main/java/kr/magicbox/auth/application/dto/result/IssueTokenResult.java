package kr.magicbox.auth.application.dto.result;

import lombok.Builder;

@Builder
public record IssueTokenResult(
        String accessToken,
        String refreshToken
) {
}
