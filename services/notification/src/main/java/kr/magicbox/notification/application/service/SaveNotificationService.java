package kr.magicbox.notification.application.service;

import kr.magicbox.notification.application.dto.command.SaveNotificationCommand;
import kr.magicbox.notification.application.port.in.SaveNotificationUseCase;
import kr.magicbox.notification.application.port.out.NotificationRepositoryPort;
import kr.magicbox.notification.domain.aggregate.Notification;
import kr.magicbox.notification.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveNotificationService implements SaveNotificationUseCase {

    private final NotificationRepositoryPort notificationRepositoryPort;

    @Transactional
    @Override
    public void save(SaveNotificationCommand command) {
        Notification notification = Notification.create(
                UserId.of(command.userId()),
                command.type()
        );
        notificationRepositoryPort.save(notification);
    }
}
