package kr.magicbox.creator.adapter.in.web.dto.response;

import java.util.List;
import java.util.function.Function;

public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext
) {

    public static <T> CursorResponse<T> of(List<T> content, int size, Function<T, Long> cursorExtractor) {
        boolean hasNext = content.size() > size;
        List<T> sliced = hasNext ? content.subList(0, size) : content;
        Long nextCursor = hasNext ? cursorExtractor.apply(sliced.getLast()) : null;
        return new CursorResponse<>(sliced, nextCursor, hasNext);
    }
}