package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.ReviewCertificationCommand;
import kr.magicbox.creator.application.port.in.ReviewCertificationUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorDomainEventRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import kr.magicbox.creator.application.port.out.UserNicknameQueryPort;
import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.event.CertificationApprovedEvent;
import kr.magicbox.creator.domain.event.CertificationRejectedEvent;
import kr.magicbox.creator.domain.exception.CertificationNotFoundException;
import kr.magicbox.creator.domain.vo.CreatorCertificationResult;
import kr.magicbox.creator.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ReviewCertificationService implements ReviewCertificationUseCase {

    private final CreatorCertificationRepositoryPort certificationRepositoryPort;
    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort eventRepositoryPort;
    private final UserNicknameQueryPort userNicknameQueryPort;

    @Transactional
    @Override
    public void reviewCertification(ReviewCertificationCommand command) {
        CreatorCertification certification = certificationRepositoryPort.findById(command.certificationId())
                .orElseThrow(CertificationNotFoundException::new);

        certification.review(command.certificationStatus(), CreatorCertificationResult.of(command.reviewMessage()));
        certificationRepositoryPort.update(certification);

        if (certification.isApproved()) {
            createCreator(certification);
            eventRepositoryPort.save(buildApprovedEvent(certification));
        }
        else {
            eventRepositoryPort.save(buildRejectedEvent(certification));
        }
    }

    private CertificationApprovedEvent buildApprovedEvent(CreatorCertification certification) {
        return CertificationApprovedEvent.builder()
                .userId(certification.getUserId())
                .certificationId(certification.getId())
                .reviewedAt(Instant.now())
                .build();
    }

    private CertificationRejectedEvent buildRejectedEvent(CreatorCertification certification) {
        return CertificationRejectedEvent.builder()
                .userId(certification.getUserId())
                .certificationId(certification.getId())
                .reviewMessage(certification.getResult().reviewMessage())
                .reviewedAt(Instant.now())
                .build();
    }

    private void createCreator(CreatorCertification certification) {
        String nickname = userNicknameQueryPort.getNickname(certification.getUserId());

        Creator creator = Creator.builder()
                .userId(certification.getUserId())
                .nickname(Nickname.of(nickname))
                .genres(certification.getRequest().genres())
                .build();

        creatorRepositoryPort.save(creator);
    }
}
