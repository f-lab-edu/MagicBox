package kr.magicbox.user.application.service;

import kr.magicbox.user.application.port.in.UnbanUserUseCase;
import kr.magicbox.user.application.port.out.UserDomainEventRepositoryPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.event.UserUnbannedEvent;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UnbanUserService implements UnbanUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserDomainEventRepositoryPort userDomainEventRepositoryPort;

    @Override
    @Transactional
    public void unbanUser(Nickname nickname) {
        User user = userRepositoryPort.getUserByNicknameWithLock(nickname)
                .orElseThrow(UserNotFoundException::new);

        user.unban();
        userRepositoryPort.update(user);

        UserUnbannedEvent event = UserUnbannedEvent.builder()
                .userId(user.getId())
                .unbannedAt(Instant.now())
                .build();
        userDomainEventRepositoryPort.save(event);
    }
}
