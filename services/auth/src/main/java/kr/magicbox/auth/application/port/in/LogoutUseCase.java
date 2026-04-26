package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.LogoutCommand;

public interface LogoutUseCase {
    void logout(LogoutCommand command);
}
