package kr.magicbox.search.application.dto.result;

import kr.magicbox.search.adapter.out.elasticsearch.document.ReleaseDocument;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ReleaseSearchResult(
        Long releaseId,
        Long creatorId,
        String title,
        String description,
        String level,
        Long price,
        Integer limitedQuantity,
        List<String> mediaUrls,
        Instant scheduledAt,
        Instant createdAt
) {
    public static ReleaseSearchResult from(ReleaseDocument doc) {
        return ReleaseSearchResult.builder()
                .releaseId(doc.getReleaseId())
                .creatorId(doc.getCreatorId())
                .title(doc.getTitle())
                .description(doc.getDescription())
                .level(doc.getLevel())
                .price(doc.getPrice())
                .limitedQuantity(doc.getLimitedQuantity())
                .mediaUrls(doc.getMediaUrls())
                .scheduledAt(doc.getScheduledAt())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
