package kr.magicbox.creator.adapter.in.web.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext
) {
}
