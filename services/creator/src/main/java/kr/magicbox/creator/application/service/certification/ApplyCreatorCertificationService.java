package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.ApplyCertificationCommand;
import kr.magicbox.creator.application.port.in.ApplyCreatorCertificationUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.vo.CreatorCertificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplyCreatorCertificationService implements ApplyCreatorCertificationUseCase {

    private final CreatorCertificationRepositoryPort certificationRepositoryPort;

    @Transactional
    @Override
    public void applyCreatorCertification(ApplyCertificationCommand command) {
        log.info("서비스 통과");
        List<CreatorCertification> existingCertifications =
                certificationRepositoryPort.findAllByUserId(command.userId());
        log.info("서비스 통과1, {}", existingCertifications.size());

        CreatorCertificationRequest request = CreatorCertificationRequest.builder()
                .genres(command.genres())
                .portfolioUrl(command.portfolioUrl())
                .build();

        CreatorCertification certification = CreatorCertification.create(command.userId(), request, existingCertifications);

        certificationRepositoryPort.save(certification);
    }
}
