package kr.magicbox.creator.application.dto.query;

import lombok.Builder;

@Builder
public record GetAllPendingCertificationsQuery(
        Long cursorId,
        int size
) {
    public static GetAllPendingCertificationsQuery of(Long cursorId, int size) {
        return new GetAllPendingCertificationsQuery(cursorId, size);
    }
}
