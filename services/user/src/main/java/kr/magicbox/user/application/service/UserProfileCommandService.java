package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandService implements UserProfileCommandUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileCommand command) {
        User user = userRepositoryPort.getUserByNickname(command.beforeNickname())
                .orElseThrow(() -> new UserNotFoundException(command.beforeNickname()));

        user.updateProfile(Nickname.of(command.nickname()), command.profile());

        userRepositoryPort.updateUser(user);
    }
}
