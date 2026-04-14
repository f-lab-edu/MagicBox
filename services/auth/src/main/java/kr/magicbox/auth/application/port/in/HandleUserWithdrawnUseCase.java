package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.UserWithdrawnCommand;

public interface HandleUserWithdrawnUseCase {
    void handleUserWithdrawn(UserWithdrawnCommand command);
}
