package kr.magicbox.notification.application.port.in;

import kr.magicbox.notification.application.dto.command.RegisterFcmTokenCommand;

public interface RegisterFcmTokenUseCase {
    void register(RegisterFcmTokenCommand command);
}
