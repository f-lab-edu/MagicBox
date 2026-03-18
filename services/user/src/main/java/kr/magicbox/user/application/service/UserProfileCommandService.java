package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.out.persistence.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.repository.UserRepository;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandService implements UserProfileCommandUseCase {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileCommand command) {
        User user = userRepository.getUserByNickname(command.beforeNickname())
                .orElseThrow(() -> new UserNotFoundException(command.beforeNickname()));

        user.updateProfile(Nickname.of(command.nickname()), command.profile());

        userRepository.updateUser(user);
    }
}
