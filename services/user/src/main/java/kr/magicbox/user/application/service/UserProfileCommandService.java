package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.domain.repository.UserRepository;
import kr.magicbox.user.domain.entity.UserEntity;
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
        UserEntity user = userRepository.getUserByNickname(command.beforeNickname())
                .orElseThrow(() -> new UserNotFoundException(command.beforeNickname()));
        
        if (command.nickname() != null) {
            user.updateNickname(command.nickname());
        }
        
        if (command.profile() != null) {
            user.updateProfile(command.profile());
        }
        
        userRepository.updateUser(user);
    }
}
