package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.HandleUserBannedCommand;

public interface HandleUserBannedUseCase {
    void handleUserBanned(HandleUserBannedCommand command);
}
