package kr.magicbox.user.application.dto.command;

public record SignupWithEmailCommand(
        String email,
        String passwordHash,
        String nickname
) {
    public static SignupWithEmailCommand of(String email, String passwordHash, String nickname) {
        return new SignupWithEmailCommand(email, passwordHash, nickname);
    }
}
