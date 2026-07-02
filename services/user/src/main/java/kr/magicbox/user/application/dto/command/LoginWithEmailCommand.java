package kr.magicbox.user.application.dto.command;

public record LoginWithEmailCommand(
        String email,
        String rawPassword
) {
    public static LoginWithEmailCommand of(String email, String rawPassword) {
        return new LoginWithEmailCommand(email, rawPassword);
    }
}
