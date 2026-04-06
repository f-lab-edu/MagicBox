package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.ApplyCertificationCommand;
import kr.magicbox.creator.application.port.in.ApplyCreatorCertificationUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.vo.CreatorCertificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyCreatorCertificationService implements ApplyCreatorCertificationUseCase {

    private final CreatorCertificationRepositoryPort certificationRepositoryPort;

    @Transactional
    @Override
    public void applyCreatorCertification(ApplyCertificationCommand command) {
        List<CreatorCertification> existingCertifications =
                certificationRepositoryPort.findAllByUserId(command.userId());

        CreatorCertificationRequest request = CreatorCertificationRequest.builder()
                .genres(command.genres())
                .portfolioUrl(command.portfolioUrl())
                .build();

        CreatorCertification certification = CreatorCertification.create(command.userId(), request, existingCertifications);

        certificationRepositoryPort.save(certification);
    }
}
