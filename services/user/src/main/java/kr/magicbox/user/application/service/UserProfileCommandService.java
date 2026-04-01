package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.exception.DuplicateNicknameException;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandService implements UserProfileCommandUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void updateUserProfile(UserId userId, UpdateUserProfileCommand command) {
        User user = userRepositoryPort.getUserById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (command.nickname() != null) {
            userRepositoryPort.getUserByNickname(command.nickname())
                    .filter(found -> !found.getId().equals(userId))
                    .ifPresent(found -> { throw new DuplicateNicknameException(command.nickname()); });
        }

        user.updateProfile(
                command.nickname() != null ? Nickname.of(command.nickname()) : null,
                command.profile(),
                command.isReviewVisible()
        );

        userRepositoryPort.updateUser(user);
    }
}