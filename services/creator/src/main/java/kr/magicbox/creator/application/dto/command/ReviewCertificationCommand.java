package kr.magicbox.creator.application.dto.command;

import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;

public record ReviewCertificationCommand(
        CreatorCertificationId certificationId,
        CreatorCertificationStatus certificationStatus,
        String reviewMessage
) {
}
