package kr.magicbox.creator.application.dto.query;

import kr.magicbox.creator.domain.vo.UserId;
import lombok.Builder;

@Builder
public record GetMyCertificationsQuery(
        UserId userId,
        Long cursorId,
        int size
) {
    public static GetMyCertificationsQuery of(UserId userId, Long cursorId, int size) {
        return new GetMyCertificationsQuery(userId, cursorId, size);
    }
}
