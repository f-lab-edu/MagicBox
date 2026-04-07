package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.ApplyCreatorCertificationCommand;
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
    public void applyCreatorCertification(ApplyCreatorCertificationCommand command) {
        List<CreatorCertification> existingCreatorCertifications =
                certificationRepositoryPort.findAllByUserId(command.userId());

        CreatorCertificationRequest request = CreatorCertificationRequest.builder()
                .genres(command.genres())
                .portfolioUrl(command.portfolioUrl())
                .build();

        CreatorCertification certification = CreatorCertification.create(command.userId(), request, existingCreatorCertifications);

        certificationRepositoryPort.save(certification);
    }
}
