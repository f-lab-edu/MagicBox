package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.command.LoginWithEmailCommand;
import kr.magicbox.user.application.dto.result.EmailCredentialResult;
import kr.magicbox.user.application.port.in.LoginWithEmailUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.enums.UserStatus;
import kr.magicbox.user.domain.exception.InvalidPasswordException;
import kr.magicbox.user.domain.exception.UserBannedException;
import kr.magicbox.user.domain.exception.UserDeletedException;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginWithEmailService implements LoginWithEmailUseCase {

    private final UserRepositoryPort userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public EmailCredentialResult loginWithEmail(LoginWithEmailCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(UserNotFoundException::new);

        if (UserStatus.BANNED.equals(user.getStatus())) throw new UserBannedException();
        if (UserStatus.DELETED.equals(user.getStatus())) throw new UserDeletedException();

        if (!passwordEncoder.matches(command.rawPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException();
        }

        return EmailCredentialResult.builder()
                .userId(user.getId())
                .userRole(user.getRole())
                .build();
    }
}
