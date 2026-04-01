package kr.magicbox.user.adapter.in.kafka;

import kr.magicbox.user.adapter.in.kafka.event.LoginEvent;
import kr.magicbox.user.adapter.in.kafka.event.LogoutEvent;
import kr.magicbox.user.application.port.in.ManageUserSessionUseCase;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuthEventKafkaListener {
    private final ManageUserSessionUseCase manageUserSessionUseCase;

    @KafkaListener(topics = "outbox.event.user-logged-in", groupId = "user-service")
    public void handleLoginEvent(ConsumerRecord<String, LoginEvent> record) {
        Instant eventAt = Instant.ofEpochMilli(record.timestamp());
        manageUserSessionUseCase.startSession(UserId.of(record.value().userId()), eventAt);
    }

    @KafkaListener(topics = "outbox.event.user-logged-out", groupId = "user-service")
    public void handleLogoutEvent(ConsumerRecord<String, LogoutEvent> record) {
        Instant eventAt = Instant.ofEpochMilli(record.timestamp());
        manageUserSessionUseCase.endSession(UserId.of(record.value().userId()), eventAt);
    }
}