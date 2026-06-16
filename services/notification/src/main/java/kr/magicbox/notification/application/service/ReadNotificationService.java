package kr.magicbox.notification.application.service;

import kr.magicbox.notification.application.dto.command.ReadNotificationCommand;
import kr.magicbox.notification.application.port.in.ReadNotificationUseCase;
import kr.magicbox.notification.application.port.out.NotificationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadNotificationService implements ReadNotificationUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    @Transactional
    @Override
    public void readAll(ReadNotificationCommand command) {
        notificationRepositoryPort.updateAllByIdsAndUserId(command.notificationIds(), command.userId());
    }
}
