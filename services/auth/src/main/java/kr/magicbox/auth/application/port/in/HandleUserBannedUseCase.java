package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.UserBannedCommand;

public interface HandleUserBannedUseCase {
    void handleUserBanned(UserBannedCommand command);
}
