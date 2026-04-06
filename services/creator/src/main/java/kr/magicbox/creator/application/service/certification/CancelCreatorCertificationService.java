package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.CancelCreatorCertificationCommand;
import kr.magicbox.creator.application.port.in.CancelCreatorCertificationUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.exception.CertificationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelCreatorCertificationService implements CancelCreatorCertificationUseCase {

    private final CreatorCertificationRepositoryPort certificationRepositoryPort;

    @Override
    @Transactional
    public void cancelCreatorCertification(CancelCreatorCertificationCommand command) {
        CreatorCertification certification = certificationRepositoryPort.findById(command.creatorCertificationId())
                .orElseThrow(CertificationNotFoundException::new);

        certification.validateCancellable(command.userId());

        certificationRepositoryPort.deleteById(command.creatorCertificationId());
    }
}
