package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.command.HandleCreatorRevokedCommand;

public interface HandleCreatorRevokedUseCase {
    void handleCreatorRevoked(HandleCreatorRevokedCommand command);
}
