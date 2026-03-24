package kr.magicbox.user.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.domain.constants.UserPolicyConstants;
import kr.magicbox.user.adapter.in.web.validation.OptionalNotBlank;
import org.hibernate.validator.constraints.Length;

public record UpdateUserProfileRequest(
        @OptionalNotBlank(message = "닉네임이 제공된 경우 비어있을 수 없습니다.")
        @Length(min = UserPolicyConstants.nicknameMinLength, max = UserPolicyConstants.nicknameMaxLength, message = "닉네임은 " + UserPolicyConstants.nicknameMinLength + "자 이상 " + UserPolicyConstants.nicknameMaxLength + "자 이하여야 합니다.")
        String nickname,

        @OptionalNotBlank(message = "프로필이 제공된 경우 비어있을 수 없습니다.")
        String profile,

        Boolean isReviewVisible
) {
    public UpdateUserProfileCommand toCommand() {
        return UpdateUserProfileCommand.builder()
                .nickname(nickname)
                .profile(profile)
                .isReviewVisible(isReviewVisible)
                .build();
    }
}