package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.domain.vo.UserId;

public interface UserProfileCommandUseCase {
    void updateUserProfile(UserId userId, UpdateUserProfileCommand command);
}