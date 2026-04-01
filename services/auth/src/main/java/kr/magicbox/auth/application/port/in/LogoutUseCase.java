package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.LogoutCommand;

public interface LogoutUseCase {
    void logout(LogoutCommand command);
}