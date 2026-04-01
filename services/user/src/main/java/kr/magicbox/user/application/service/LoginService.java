package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.LoadUserCredentialCommand;
import kr.magicbox.user.application.dto.LoadUserCredentialResult;
import kr.magicbox.user.application.port.in.LoadUserCredentialUseCase;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.enums.UserStatus;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.global.properties.UserDefaultProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService implements LoadUserCredentialUseCase {

    private final UserRepositoryPort userRepository;
    private final UserDefaultProperties userDefaultProperties;

    @Override
    @Transactional
    public LoadUserCredentialResult loadUserCredential(LoadUserCredentialCommand command) {
        return userRepository.findByOauth2IdAndProvider(command.oauth2Id(), command.provider())
                .map(this::handleExistingUser)
                .orElseGet(() -> handleNewUser(command));
    }

    private LoadUserCredentialResult handleExistingUser(User user) {
        userRepository.updateUser(user);
        return LoadUserCredentialResult.builder()
                .userId(user.getId())
                .userRole(user.getRole())
                .build();
    }

    private LoadUserCredentialResult handleNewUser(LoadUserCredentialCommand command) {
        String nickname = UUID.randomUUID().toString().replace("-", "").substring(0, 18);
        User user = User.builder()
                .nickname(Nickname.of(nickname))
                .email(command.email())
                .status(UserStatus.ACTIVE)
                .profile(command.profileImage() != null ? command.profileImage() : userDefaultProperties.getDefaultProfileImageUrl())
                .oauth2Id(command.oauth2Id())
                .oauth2Provider(command.provider())
                .build();
        User saved = userRepository.saveUser(user);
        return LoadUserCredentialResult.builder()
                .userId(saved.getId())
                .userRole(saved.getRole())
                .build();
    }
}