package kr.magicbox.search.application.dto.result;

import kr.magicbox.search.adapter.out.elasticsearch.document.CreatorDocument;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record CreatorSearchResult(
        Long creatorId,
        String nickname,
        String tagline,
        String profileImageUrl,
        List<String> genres,
        Instant createdAt
) {
    public static CreatorSearchResult from(CreatorDocument doc) {
        return CreatorSearchResult.builder()
                .creatorId(doc.getCreatorId())
                .nickname(doc.getNickname())
                .tagline(doc.getTagline())
                .profileImageUrl(doc.getProfileImageUrl())
                .genres(doc.getGenres())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
