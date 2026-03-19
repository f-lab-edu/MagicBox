package kr.magicbox.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IssueTokenCommand(
    @NotNull(message = "코드는 필수 값입니다.")
    @NotBlank(message = "코드는 필수 값입니다.")
    String code
) {
}