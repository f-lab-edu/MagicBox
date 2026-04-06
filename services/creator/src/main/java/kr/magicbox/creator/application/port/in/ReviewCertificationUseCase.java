package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.command.ReviewCertificationCommand;

public interface ReviewCertificationUseCase {

    void reviewCertification(ReviewCertificationCommand command);
}
