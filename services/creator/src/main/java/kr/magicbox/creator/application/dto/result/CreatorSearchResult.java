package kr.magicbox.creator.application.dto.result;

import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.vo.CreatorId;

public record CreatorSearchResult(
        CreatorId creatorId,
        String nickname,
        String introduction,
        String profileImageUrl,
        String tagline
) {

    public static CreatorSearchResult from(Creator creator) {
        return new CreatorSearchResult(
                creator.getId(),
                creator.getNicknameValue(),
                creator.getIntroduction(),
                creator.getProfileImageUrl(),
                creator.getTagline()
        );
    }
}
