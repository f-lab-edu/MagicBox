package kr.magicbox.auth.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.auth.application.dto.command.LoginCommand;

public record LoginRequest(
        @NotBlank(message = "코드는 필수 값입니다.")
        String code
) {
    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .code(code)
                .build();
    }
}
