package kr.magicbox.search.application.dto.query;

public record SearchCreatorsQuery(String keyword, int page, int size) {
    public static SearchCreatorsQuery of(String keyword, int page, int size) {
        return new SearchCreatorsQuery(keyword, page, size);
    }
}
