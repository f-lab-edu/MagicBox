package kr.magicbox.creator.adapter.in.web.dto.response;

import kr.magicbox.creator.application.dto.result.PendingCertificationResult;
import kr.magicbox.creator.domain.enums.MagicGenre;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record PendingCertificationResponse(
        Long certificationId,
        Long userId,
        Set<MagicGenre> genres,
        String portfolioUrl,
        Instant requestedAt
) {

    public static PendingCertificationResponse from(PendingCertificationResult result) {
        return PendingCertificationResponse.builder()
                .certificationId(result.certificationId().value())
                .userId(result.userId().value())
                .genres(result.genres())
                .portfolioUrl(result.portfolioUrl())
                .requestedAt(result.requestedAt())
                .build();
    }
}