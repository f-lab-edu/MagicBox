package kr.magicbox.search.application.dto.query;

public record SearchGeneralGoodsQuery(String keyword, int page, int size) {
    public static SearchGeneralGoodsQuery of(String keyword, int page, int size) {
        return new SearchGeneralGoodsQuery(keyword, page, size);
    }
}
