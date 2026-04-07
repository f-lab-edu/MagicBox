package kr.magicbox.creator.application.dto.result;

import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.enums.MagicGenre;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;
import kr.magicbox.creator.domain.vo.UserId;

import java.time.Instant;
import java.util.Set;

public record PendingCreatorCertificationResult(
        CreatorCertificationId certificationId,
        UserId userId,
        Set<MagicGenre> genres,
        String portfolioUrl,
        Instant requestedAt
) {

    public static PendingCreatorCertificationResult from(CreatorCertification certification) {
        return new PendingCreatorCertificationResult(
                certification.getId(),
                certification.getUserId(),
                certification.getRequest().genres(),
                certification.getRequest().portfolioUrl(),
                certification.getRequest().requestedAt()
        );
    }
}
