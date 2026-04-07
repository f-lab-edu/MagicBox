package kr.magicbox.creator.application.dto.command;

import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import kr.magicbox.creator.domain.vo.CreatorCertificationId;

public record ReviewCreatorCertificationCommand(
        CreatorCertificationId certificationId,
        CreatorCertificationStatus certificationStatus,
        String reviewMessage
) {
}
