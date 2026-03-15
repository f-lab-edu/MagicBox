package kr.magicbox.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.domain.constants.UserPolicyConstants;
import org.hibernate.validator.constraints.Length;

public record UpdateUserProfileCommand(
        @NotNull(message = "이전 닉네임은 존재해야 합니다.")
        @NotBlank(message = "이전 닉네임은 존재해야 합니다.")
        String beforeNickname,

        @NotBlank(message = "닉네임은 존재해야 합니다.")
        @Length(min = UserPolicyConstants.nicknameMinLength, max = UserPolicyConstants.nicknameMaxLength)
        String nickname,

        @NotBlank(message = "프로필은 존재해야 합니다.")
        String profile
) {
}