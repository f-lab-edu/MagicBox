package kr.magicbox.user.application.service;

import kr.magicbox.user.application.port.in.WithdrawUserUseCase;
import kr.magicbox.user.application.port.out.UserOutboxPort;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.event.UserWithdrawnEvent;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class WithdrawUserService implements WithdrawUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final UserOutboxPort eventRepositoryPort;

    @Transactional
    @Override
    public void withdrawUser(UserId userId) {
        User user = userRepositoryPort.getUserById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.delete();
        userRepositoryPort.updateUser(user);

        eventRepositoryPort.save(
                UserWithdrawnEvent.builder()
                        .userId(userId)
                        .withdrawnAt(Instant.now())
                        .build()
        );
    }
}
