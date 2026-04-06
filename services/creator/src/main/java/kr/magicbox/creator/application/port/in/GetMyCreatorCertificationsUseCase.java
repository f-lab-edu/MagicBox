package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.result.CreatorCertificationInfoResult;
import kr.magicbox.creator.application.dto.query.GetMyCertificationsQuery;

import java.util.List;

public interface GetMyCreatorCertificationsUseCase {

    List<CreatorCertificationInfoResult> getMyCertifications(GetMyCertificationsQuery query);
}
