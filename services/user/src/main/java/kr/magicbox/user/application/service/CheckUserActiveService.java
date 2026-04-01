package kr.magicbox.user.application.service;

import kr.magicbox.user.application.port.in.CheckUserActiveUseCase;
import kr.magicbox.user.application.port.out.UserRepositoryPort;
import kr.magicbox.user.domain.aggregate.User;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckUserActiveService implements CheckUserActiveUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public boolean isActive(UserId userId) {
        return userRepositoryPort.getUserById(userId)
                .map(User::isActive)
                .orElse(false);
    }
}
