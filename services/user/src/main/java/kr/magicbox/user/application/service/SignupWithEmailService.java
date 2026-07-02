package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.command.SignupWithEmailCommand;
import kr.magicbox.user.application.dto.result.EmailCredentialResult;
import kr.magicbox.user.application.port.in.SignupWithEmailUseCase;
import kr.magicbox.user.application.port.out.UserOutboxPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.enums.UserRole;
import kr.magicbox.user.domain.enums.UserStatus;
import kr.magicbox.user.domain.event.UserSignupEvent;
import kr.magicbox.user.domain.exception.DuplicateEmailException;
import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.global.properties.UserProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignupWithEmailService implements SignupWithEmailUseCase {

    private final UserRepositoryPort userRepository;
    private final UserOutboxPort userOutboxPort;
    private final UserProperties userProperties;

    @Override
    @Transactional
    public EmailCredentialResult signupWithEmail(SignupWithEmailCommand command) {
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new DuplicateEmailException();
        }

        String nickname = command.nickname() != null && !command.nickname().isBlank()
                ? command.nickname()
                : UUID.randomUUID().toString().replace("-", "").substring(0, 18);

        User user = User.createEmailBuilder()
                .nickname(Nickname.of(nickname))
                .email(command.email())
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .profile(userProperties.getDefaultProfileImageUrl())
                .passwordHash(command.passwordHash())
                .build();

        User saved = userRepository.save(user);

        userOutboxPort.save(UserSignupEvent.builder()
                .userId(saved.getId())
                .occurredAt(Instant.now())
                .build());

        return EmailCredentialResult.builder()
                .userId(saved.getId())
                .userRole(saved.getRole())
                .build();
    }
}
