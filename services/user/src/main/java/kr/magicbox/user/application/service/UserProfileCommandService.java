package kr.magicbox.user.application.service;

import kr.magicbox.user.adapter.exception.UserNotFoundException;
import kr.magicbox.user.adapter.out.persistence.entity.UserEntity;
import kr.magicbox.user.adapter.out.persistence.mapper.UserMapper;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileCommandService implements UserProfileCommandUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void updateUserProfile(UpdateUserProfileCommand command) {
        UserEntity userEntity = userRepository.getUserByNickname(command.beforeNickname())
                .orElseThrow(() -> new UserNotFoundException(command.beforeNickname()));

        User user = userMapper.toDomain(userEntity);
        user.updateProfile(command.nickname(), command.profile());
        
        userMapper.updateEntity(user, userEntity);
        userRepository.updateUser(userEntity);
    }
}
