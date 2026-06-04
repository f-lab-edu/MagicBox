package kr.magicbox.search.adapter.in.web.dto.response;

import kr.magicbox.search.application.dto.result.CreatorSearchResult;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CreatorSearchResponse(
        Long creatorId,
        String nickname,
        String tagline,
        String profileImageUrl,
        List<String> genres,
        Instant createdAt
) {
    public static CreatorSearchResponse from(CreatorSearchResult result) {
        return CreatorSearchResponse.builder()
                .creatorId(result.creatorId())
                .nickname(result.nickname())
                .tagline(result.tagline())
                .profileImageUrl(result.profileImageUrl())
                .genres(result.genres())
                .createdAt(result.createdAt())
                .build();
    }
}
