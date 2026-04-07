package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.command.HandleUserWithdrawnCommand;

public interface HandleUserWithdrawnUseCase {
    void handleUserWithdrawn(HandleUserWithdrawnCommand command);
}
