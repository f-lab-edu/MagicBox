package kr.magicbox.auth.adapter.in.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.magicbox.auth.application.dto.command.EmailLoginCommand;

public record EmailLoginRequest(
        @NotBlank(message = "이메일은 필수 값입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 값입니다.")
        String password
) {
    public EmailLoginCommand toCommand() {
        return EmailLoginCommand.builder()
                .email(email)
                .password(password)
                .build();
    }
}
