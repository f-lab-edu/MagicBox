package kr.magicbox.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.auth.application.dto.IssueTokenCommand;

public record IssueTokenRequest(
        @NotNull(message = "코드는 필수 값입니다.")
        @NotBlank(message = "코드는 필수 값입니다.")
        String code
) {
    public IssueTokenCommand toCommand() {
        return IssueTokenCommand.builder()
                .code(code)
                .build();
    }
}