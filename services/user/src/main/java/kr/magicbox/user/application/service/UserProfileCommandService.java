package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryOutPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandService implements UserProfileCommandUseCase {
    private final UserRepositoryOutPort userRepositoryOutPort;

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileCommand command) {
        User user = userRepositoryOutPort.getUserByNickname(command.beforeNickname())
                .orElseThrow(() -> new UserNotFoundException(command.beforeNickname()));

        user.updateProfile(Nickname.of(command.nickname()), command.profile());

        userRepositoryOutPort.updateUser(user);
    }
}
