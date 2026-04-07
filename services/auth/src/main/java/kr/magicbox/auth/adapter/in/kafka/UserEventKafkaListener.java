package kr.magicbox.auth.adapter.in.kafka;

import kr.magicbox.auth.adapter.in.kafka.event.UserBannedEvent;
import kr.magicbox.auth.adapter.in.kafka.event.UserWithdrawnEvent;
import kr.magicbox.auth.application.dto.command.HandleUserBannedCommand;
import kr.magicbox.auth.application.dto.command.HandleUserWithdrawnCommand;
import kr.magicbox.auth.application.port.in.HandleUserBannedUseCase;
import kr.magicbox.auth.application.port.in.HandleUserWithdrawnUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventKafkaListener {
    private final HandleUserWithdrawnUseCase handleUserWithdrawnUseCase;
    private final HandleUserBannedUseCase handleUserBannedUseCase;

    @KafkaListener(topics = "outbox.event.user-withdrawn", groupId = "auth-service")
    public void handleUserWithdrawnEvent(ConsumerRecord<String, UserWithdrawnEvent> consumerRecord) {
        UserWithdrawnEvent event = consumerRecord.value();
        handleUserWithdrawnUseCase.handleUserWithdrawn(HandleUserWithdrawnCommand.of(event.userId()));
    }

    @KafkaListener(topics = "outbox.event.user-banned", groupId = "auth-service")
    public void handleUserBannedEvent(ConsumerRecord<String, UserBannedEvent> consumerRecord) {
        UserBannedEvent event = consumerRecord.value();
        handleUserBannedUseCase.handleUserBanned(HandleUserBannedCommand.of(event.userId()));
    }
}
