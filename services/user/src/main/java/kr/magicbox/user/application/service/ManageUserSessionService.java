package kr.magicbox.user.application.service;

import kr.magicbox.user.application.dto.command.EndSessionCommand;
import kr.magicbox.user.application.dto.command.StartSessionCommand;
import kr.magicbox.user.application.port.in.ManageUserSessionUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.exception.UserAlreadyActiveException;
import kr.magicbox.user.domain.exception.UserAlreadyInactiveException;
import kr.magicbox.user.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageUserSessionService implements ManageUserSessionUseCase {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional
    public void startSession(StartSessionCommand command) {
        log.info("시작 세션 :{}", command.userId().value().toString());
        User user = userRepositoryPort.getUserById(command.userId()).orElseThrow(UserNotFoundException::new);
        if (user.isActive()) throw new UserAlreadyActiveException();
        user.startSession(command.loginAt());
        userRepositoryPort.updateUser(user);
    }

    @Override
    @Transactional
    public void endSession(EndSessionCommand command) {
        User user = userRepositoryPort.getUserById(command.userId()).orElseThrow(UserNotFoundException::new);
        if (!user.isActive()) throw new UserAlreadyInactiveException();
        user.endSession(command.logoutAt());
        userRepositoryPort.updateUser(user);
    }
}