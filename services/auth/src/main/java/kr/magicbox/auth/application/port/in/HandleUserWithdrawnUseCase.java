package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.HandleUserWithdrawnCommand;

public interface HandleUserWithdrawnUseCase {
    void handleUserWithdrawn(HandleUserWithdrawnCommand command);
}
