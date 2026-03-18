package kr.magicbox.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.domain.constants.UserPolicyConstants;
import kr.magicbox.user.global.validation.AtLeastOneNotNull;
import kr.magicbox.user.global.validation.OptionalNotBlank;
import org.hibernate.validator.constraints.Length;

@AtLeastOneNotNull(fields = {"nickname", "profile"}, message = "닉네임 또는 프로필 중 최소 하나는 제공되어야 합니다.")
public record UpdateUserProfileCommand(
        @NotNull(message = "이전 닉네임은 존재해야 합니다.")
        @NotBlank(message = "이전 닉네임은 존재해야 합니다.")
        String beforeNickname,

        @OptionalNotBlank(message = "닉네임은 존재해야 합니다.")
        @Length(min = UserPolicyConstants.nicknameMinLength, max = UserPolicyConstants.nicknameMaxLength, message = "닉네임은 " + UserPolicyConstants.nicknameMinLength + "자 이상 " + UserPolicyConstants.nicknameMaxLength + "자 이하여야 합니다.")
        String nickname,

        @OptionalNotBlank(message = "프로필이 제공된 경우 비어있을 수 없습니다.")
        String profile
) {
}