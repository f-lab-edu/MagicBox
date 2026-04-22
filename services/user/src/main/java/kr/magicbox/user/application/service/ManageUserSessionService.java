package kr.magicbox.user.application.service;

import kr.magicbox.user.application.port.in.ManageUserSessionUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.exception.UserAlreadyActiveException;
import kr.magicbox.user.domain.exception.UserAlreadyInactiveException;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageUserSessionService implements ManageUserSessionUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void startSession(UserId userId, Instant loginAt) {
        log.info("시작 세션 :{}", userId.value().toString());
        User user = userRepositoryPort.getUserById(userId).orElseThrow(UserNotFoundException::new);
        if (user.isActive()) throw new UserAlreadyActiveException();
        user.startSession(loginAt);
        userRepositoryPort.updateUser(user);
    }

    @Override
    @Transactional
    public void endSession(UserId userId, Instant logoutAt) {
        User user = userRepositoryPort.getUserById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.isActive()) throw new UserAlreadyInactiveException();
        user.endSession(logoutAt);
        userRepositoryPort.updateUser(user);
    }
}