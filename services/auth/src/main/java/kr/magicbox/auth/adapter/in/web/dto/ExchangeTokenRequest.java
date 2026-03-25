package kr.magicbox.auth.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.auth.application.dto.ExchangeTokenCommand;

public record ExchangeTokenRequest(
        @NotBlank(message = "코드는 필수 값입니다.")
        String code
) {
    public ExchangeTokenCommand toCommand() {
        return ExchangeTokenCommand.builder()
                .code(code)
                .build();
    }
}