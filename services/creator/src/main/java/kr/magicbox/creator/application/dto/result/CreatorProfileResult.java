package kr.magicbox.creator.application.dto.result;

import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.enums.MagicGenre;

import java.util.Set;

public record CreatorProfileResult(
        String nickname,
        String tagline,
        String profileImageUrl,
        String introduction,
        Set<MagicGenre> genres
) {
    public static CreatorProfileResult from(Creator creator) {
        return new CreatorProfileResult(
                creator.getNicknameValue(),
                creator.getTagline(),
                creator.getProfileImageUrl(),
                creator.getIntroduction(),
                creator.getGenres()
        );
    }
}
