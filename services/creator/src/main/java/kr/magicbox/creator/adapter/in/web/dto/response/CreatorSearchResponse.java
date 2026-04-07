package kr.magicbox.creator.adapter.in.web.dto.response;

import kr.magicbox.creator.application.dto.result.CreatorSearchResult;
import lombok.Builder;

@Builder
public record CreatorSearchResponse(
        Long creatorId,
        String nickname,
        String introduction,
        String profileImageUrl,
        String tagline
) {

    public static CreatorSearchResponse from(CreatorSearchResult result) {
        return CreatorSearchResponse.builder()
                .creatorId(result.creatorId().value())
                .nickname(result.nickname())
                .introduction(result.introduction())
                .profileImageUrl(result.profileImageUrl())
                .tagline(result.tagline())
                .build();
    }
}