package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.query.GetMyProfileQuery;
import kr.magicbox.user.application.dto.result.GetMyProfileResult;

public interface GetMyProfileUseCase {
    GetMyProfileResult getMyProfile(GetMyProfileQuery query);
}
