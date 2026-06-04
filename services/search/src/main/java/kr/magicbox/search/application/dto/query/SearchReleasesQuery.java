package kr.magicbox.search.application.dto.query;

public record SearchReleasesQuery(String keyword, int page, int size) {
    public static SearchReleasesQuery of(String keyword, int page, int size) {
        return new SearchReleasesQuery(keyword, page, size);
    }
}
