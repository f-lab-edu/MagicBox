package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.query.GetAllPendingCertificationsQuery;
import kr.magicbox.creator.application.dto.result.PendingCertificationResult;

import java.util.List;

public interface GetAllPendingCreatorCertificationsUseCase {

    List<PendingCertificationResult> getAllPendingCreatorCertifications(GetAllPendingCertificationsQuery query);
}
