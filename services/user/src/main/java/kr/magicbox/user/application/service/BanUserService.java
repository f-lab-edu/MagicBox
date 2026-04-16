package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.command.BanUserCommand;
import kr.magicbox.user.application.port.in.BanUserUseCase;
import kr.magicbox.user.application.port.out.UserDomainEventRepositoryPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.event.UserBannedEvent;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BanUserService implements BanUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserDomainEventRepositoryPort userDomainEventRepositoryPort;

    @Override
    @Transactional
    public void banUser(BanUserCommand command) {
        User user = userRepositoryPort.getUserByNicknameWithLock(command.nickname())
                .orElseThrow(UserNotFoundException::new);

        user.ban();
        userRepositoryPort.update(user);

        UserBannedEvent event = UserBannedEvent.builder()
                .userId(user.getId())
                .bannedAt(Instant.now())
                .build();
        userDomainEventRepositoryPort.save(event);
    }
}
