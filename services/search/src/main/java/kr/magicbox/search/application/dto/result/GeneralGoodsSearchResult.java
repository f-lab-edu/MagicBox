package kr.magicbox.search.application.dto.result;

import kr.magicbox.search.adapter.out.elasticsearch.document.GeneralGoodsDocument;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record GeneralGoodsSearchResult(
        Long generalGoodsId,
        Long creatorId,
        String name,
        Long price,
        Long stock,
        List<String> mediaUrls,
        Instant createdAt
) {
    public static GeneralGoodsSearchResult from(GeneralGoodsDocument doc) {
        return GeneralGoodsSearchResult.builder()
                .generalGoodsId(doc.getGeneralGoodsId())
                .creatorId(doc.getCreatorId())
                .name(doc.getName())
                .price(doc.getPrice())
                .stock(doc.getStock())
                .mediaUrls(doc.getMediaUrls())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
