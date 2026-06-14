package kr.magicbox.notification.application.port.in;

import kr.magicbox.notification.application.dto.command.ReadNotificationCommand;

public interface ReadNotificationUseCase {
    void readAll(ReadNotificationCommand command);
}
