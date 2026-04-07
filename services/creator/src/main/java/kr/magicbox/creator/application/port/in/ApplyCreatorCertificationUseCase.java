package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.command.ApplyCertificationCommand;

public interface ApplyCreatorCertificationUseCase {

    void applyCreatorCertification(ApplyCertificationCommand command);
}