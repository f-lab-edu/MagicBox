package kr.magicbox.auth.application.dto.command;

import lombok.Builder;

@Builder
public record RefreshTokenCommand(
        String refreshToken
) {
    public static RefreshTokenCommand of(String refreshToken) {
        return new RefreshTokenCommand(refreshToken);
    }
}
