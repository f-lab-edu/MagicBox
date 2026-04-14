package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.command.ReviewCertificationCommand;
import kr.magicbox.creator.application.port.in.ReviewCreatorCertificationUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorDomainEventRepositoryPort;
import kr.magicbox.creator.application.port.out.CreatorRepositoryPort;
import kr.magicbox.creator.application.port.out.UserNicknameQueryPort;
import kr.magicbox.creator.domain.aggregate.Creator;
import kr.magicbox.creator.domain.aggregate.CreatorCertification;
import kr.magicbox.creator.domain.event.CertificationApprovedEvent;
import kr.magicbox.creator.domain.event.CertificationRejectedEvent;
import kr.magicbox.creator.domain.exception.CertificationNotFoundException;
import kr.magicbox.creator.domain.exception.CreatorAlreadyExistsException;
import kr.magicbox.creator.domain.vo.CreatorCertificationResult;
import kr.magicbox.creator.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCertificationService implements ReviewCreatorCertificationUseCase {

    private final CreatorCertificationRepositoryPort certificationRepositoryPort;
    private final CreatorRepositoryPort creatorRepositoryPort;
    private final CreatorDomainEventRepositoryPort eventRepositoryPort;
    private final UserNicknameQueryPort userNicknameQueryPort;

    @Transactional
    @Override
    public void reviewCreatorCertification(ReviewCertificationCommand command) {
        CreatorCertification certification = certificationRepositoryPort.findById(command.certificationId())
                .orElseThrow(CertificationNotFoundException::new);

        certification.review(command.certificationStatus().toStatus(), CreatorCertificationResult.of(command.reviewerId(), command.reviewMessage()));
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
                .reviewedAt(certification.getResult().reviewedAt())
                .build();
    }

    private CertificationRejectedEvent buildRejectedEvent(CreatorCertification certification) {
        return CertificationRejectedEvent.builder()
                .userId(certification.getUserId())
                .certificationId(certification.getId())
                .reviewMessage(certification.getResult().reviewMessage())
                .reviewedAt(certification.getResult().reviewedAt())
                .build();
    }

    private void createCreator(CreatorCertification certification) {
        if(creatorRepositoryPort.existsByUserId(certification.getUserId())) throw new CreatorAlreadyExistsException();
        String nickname = userNicknameQueryPort.getNickname(certification.getUserId());

        Creator creator = Creator.builder()
                .userId(certification.getUserId())
                .nickname(Nickname.of(nickname))
                .genres(certification.getRequest().genres())
                .build();

        creatorRepositoryPort.save(creator);
    }
}
