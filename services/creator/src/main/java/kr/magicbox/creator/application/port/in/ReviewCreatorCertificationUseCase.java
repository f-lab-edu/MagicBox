package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.command.ReviewCertificationCommand;

public interface ReviewCreatorCertificationUseCase {

    void reviewCertification(ReviewCertificationCommand command);
}
