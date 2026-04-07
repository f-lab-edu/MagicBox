package kr.magicbox.user.adapter.in.kafka;

import kr.magicbox.user.adapter.in.kafka.event.LoginEvent;
import kr.magicbox.user.adapter.in.kafka.event.LogoutEvent;
import kr.magicbox.user.application.dto.command.EndSessionCommand;
import kr.magicbox.user.application.dto.command.StartSessionCommand;
import kr.magicbox.user.application.port.in.ManageUserSessionUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEventKafkaListener {
    private final ManageUserSessionUseCase manageUserSessionUseCase;

    @KafkaListener(topics = "outbox.event.user-logged-in", groupId = "user-service")
    public void handleLoginEvent(ConsumerRecord<String, LoginEvent> record) {
        LoginEvent loginEvent = record.value();
        manageUserSessionUseCase.startSession(StartSessionCommand.of(loginEvent.userId(), loginEvent.createdAt()));
    }

    @KafkaListener(topics = "outbox.event.user-logged-out", groupId = "user-service")
    public void handleLogoutEvent(ConsumerRecord<String, LogoutEvent> record) {
        LogoutEvent logoutEvent = record.value();
        manageUserSessionUseCase.endSession(EndSessionCommand.of(logoutEvent.userId(), logoutEvent.createdAt()));
    }
}
