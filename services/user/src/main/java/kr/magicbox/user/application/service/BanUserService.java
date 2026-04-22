package kr.magicbox.user.application.service;

import kr.magicbox.user.application.port.in.BanUserUseCase;
import kr.magicbox.user.application.port.out.UserOutboxPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.event.UserBannedEvent;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BanUserService implements BanUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserOutboxPort userOutboxPort;

    @Override
    @Transactional
    public void banUser(Nickname nickname) {
        User user = userRepositoryPort.getUserByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);

        user.ban();
        userRepositoryPort.updateUser(user);

        UserBannedEvent event = UserBannedEvent.builder()
                .userId(user.getId())
                .bannedAt(Instant.now())
                .build();
        userOutboxPort.save(event);
    }
}
