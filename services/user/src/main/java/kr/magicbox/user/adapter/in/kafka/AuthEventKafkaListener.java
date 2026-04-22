package kr.magicbox.user.adapter.in.kafka;

import kr.magicbox.user.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.user.adapter.in.kafka.event.LoginEvent;
import kr.magicbox.user.adapter.in.kafka.event.LogoutEvent;
import kr.magicbox.user.application.dto.command.EndSessionCommand;
import kr.magicbox.user.application.dto.command.StartSessionCommand;
import kr.magicbox.user.application.port.in.ManageUserSessionUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthEventKafkaListener {

    private final ManageUserSessionUseCase manageUserSessionUseCase;

    @Idempotent
    @KafkaListener(topics = "outbox.event.user-logged-in", groupId = "user-service")
    public void handleLoginEvent(ConsumerRecord<String, LoginEvent> record) {
        log.info("로그인 이벤트 핸들링 함수 접근!");
        LoginEvent event = record.value();
        manageUserSessionUseCase.startSession(StartSessionCommand.of(event.userId(), event.createdAt()));
    }

    @Idempotent
    @KafkaListener(topics = "outbox.event.user-logged-out", groupId = "user-service")
    public void handleLogoutEvent(ConsumerRecord<String, LogoutEvent> record) {
        LogoutEvent event = record.value();
        manageUserSessionUseCase.endSession(EndSessionCommand.of(event.userId(), event.createdAt()));
    }
}