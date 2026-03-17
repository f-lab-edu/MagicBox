package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.UpdateUserProfileCommand;

public interface UserProfileCommandUseCase {
    void updateUserProfile(UpdateUserProfileCommand command);
}
