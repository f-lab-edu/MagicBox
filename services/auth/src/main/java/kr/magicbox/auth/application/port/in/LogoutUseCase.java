package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.domain.vo.UserId;

public interface LogoutUseCase {
    void logout(UserId userId);
}