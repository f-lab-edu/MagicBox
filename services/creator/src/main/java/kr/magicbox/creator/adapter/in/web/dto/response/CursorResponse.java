package kr.magicbox.creator.adapter.in.web.dto.response;

import java.util.List;
import java.util.function.ToLongFunction;

public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext
) {

    public static <T> CursorResponse<T> of(List<T> content, int size, ToLongFunction<T> cursorExtractor) {
        boolean hasNext = content.size() > size;
        List<T> sliced = hasNext ? content.subList(0, size) : content;
        Long nextCursor = hasNext ? cursorExtractor.applyAsLong(sliced.getLast()) : null;
        return new CursorResponse<>(sliced, nextCursor, hasNext);
    }
}
