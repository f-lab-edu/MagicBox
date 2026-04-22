package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.command.HandleUserRevokedCommand;

public interface HandleUserRevokedUseCase {
    void handleUserRevoked(HandleUserRevokedCommand command);
}
