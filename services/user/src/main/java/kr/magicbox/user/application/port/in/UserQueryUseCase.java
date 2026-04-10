package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.domain.vo.UserId;

public interface UserQueryUseCase {
    GetUserProfileResult getUserProfile(Nickname nickname, UserId requestUserId);
}
