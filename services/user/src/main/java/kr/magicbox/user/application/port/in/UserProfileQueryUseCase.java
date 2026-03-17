package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.GetUserProfileResult;

public interface UserProfileQueryUseCase {
    GetUserProfileResult getUserProfile(String nickname);
}
