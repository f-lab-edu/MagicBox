package kr.magicbox.notification.application.dto.command;

public record RegisterFcmTokenCommand(Long userId, String token) {
    public static RegisterFcmTokenCommand of(Long userId, String token) {
        return new RegisterFcmTokenCommand(userId, token);
    }
}
