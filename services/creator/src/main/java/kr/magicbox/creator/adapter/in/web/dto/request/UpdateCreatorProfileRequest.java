package kr.magicbox.creator.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.magicbox.creator.application.dto.command.UpdateCreatorProfileCommand;
import kr.magicbox.creator.domain.constants.CreatorPolicyConstants;
import kr.magicbox.creator.domain.enums.MagicGenre;
import kr.magicbox.creator.domain.vo.Nickname;
import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

import java.util.Set;

@Builder
public record UpdateCreatorProfileRequest(
        @Size(min = CreatorPolicyConstants.NICKNAME_MIN_LENGTH, max = CreatorPolicyConstants.NICKNAME_MAX_LENGTH) String nickname,
        @Size(max = 50) String tagline,
        String profileImageUrl,
        @Size(max = 500) String introduction,
        @NotEmpty Set<@NotNull MagicGenre> genres
) {

    public UpdateCreatorProfileCommand toCommand(UserId userId) {
        return new UpdateCreatorProfileCommand(
                userId,
                Nickname.of(nickname),
                tagline,
                profileImageUrl,
                introduction,
                genres
        );
    }
}
