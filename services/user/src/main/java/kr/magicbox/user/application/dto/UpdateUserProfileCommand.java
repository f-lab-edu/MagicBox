package kr.magicbox.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserProfileCommand(
        @NotNull(message = "이전 닉네임은 존재해야 합니다.")
        @NotBlank(message = "이전 닉네임은 존재해야 합니다.")
        String beforeNickname,

        String nickname,
        String profile
) {
}