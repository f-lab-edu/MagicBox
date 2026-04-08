package kr.magicbox.creator.application.dto.result;

import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.vo.CreatorId;
import kr.magicbox.creator.domain.vo.Nickname;

public record CreatorSearchResult(
        CreatorId creatorId,
        Nickname nickname,
        String introduction,
        String profileImageUrl,
        String tagline
) {

    public static CreatorSearchResult from(Creator creator) {
        return new CreatorSearchResult(
                creator.getId(),
                creator.getNickname(),
                creator.getIntroduction(),
                creator.getProfileImageUrl(),
                creator.getTagline()
        );
    }
}
