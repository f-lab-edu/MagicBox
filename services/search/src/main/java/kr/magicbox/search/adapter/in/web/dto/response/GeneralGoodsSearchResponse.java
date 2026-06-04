package kr.magicbox.search.adapter.in.web.dto.response;

import kr.magicbox.search.application.dto.result.GeneralGoodsSearchResult;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record GeneralGoodsSearchResponse(
        Long generalGoodsId,
        Long creatorId,
        String name,
        Long price,
        Long stock,
        List<String> mediaUrls,
        Instant createdAt
) {
    public static GeneralGoodsSearchResponse from(GeneralGoodsSearchResult result) {
        return GeneralGoodsSearchResponse.builder()
                .generalGoodsId(result.generalGoodsId())
                .creatorId(result.creatorId())
                .name(result.name())
                .price(result.price())
                .stock(result.stock())
                .mediaUrls(result.mediaUrls())
                .createdAt(result.createdAt())
                .build();
    }
}
