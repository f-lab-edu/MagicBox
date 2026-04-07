package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.command.HandleUserBannedCommand;

public interface HandleUserBannedUseCase {
    void handleUserBanned(HandleUserBannedCommand command);
}
