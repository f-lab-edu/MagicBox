package kr.magicbox.creator.application.service.certification;

import kr.magicbox.creator.application.dto.query.GetAllPendingCertificationsQuery;
import kr.magicbox.creator.application.dto.result.PendingCertificationResult;
import kr.magicbox.creator.application.port.in.GetAllPendingCreatorCertificationsUseCase;
import kr.magicbox.creator.application.port.out.CreatorCertificationRepositoryPort;
import kr.magicbox.creator.domain.enums.CreatorCertificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllPendingCertificationsService implements GetAllPendingCreatorCertificationsUseCase {

    private final CreatorCertificationRepositoryPort creatorCertificationRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<PendingCertificationResult> getAllPendingCreatorCertifications(GetAllPendingCertificationsQuery query) {
        return creatorCertificationRepositoryPort.findAllByStatusByCursor(CreatorCertificationStatus.PENDING, query.cursorId(), query.size() + 1)
                .stream()
                .map(PendingCertificationResult::from)
                .toList();
    }
}
