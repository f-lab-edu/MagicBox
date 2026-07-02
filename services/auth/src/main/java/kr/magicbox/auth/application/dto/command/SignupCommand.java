package kr.magicbox.auth.application.dto.command;

import lombok.Builder;

@Builder
public record SignupCommand(String email, String password, String nickname) {
}
