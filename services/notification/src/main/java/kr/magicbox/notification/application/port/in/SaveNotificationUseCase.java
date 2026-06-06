package kr.magicbox.notification.application.port.in;

import kr.magicbox.notification.application.dto.command.SaveNotificationCommand;

public interface SaveNotificationUseCase {
    void save(SaveNotificationCommand command);
}
