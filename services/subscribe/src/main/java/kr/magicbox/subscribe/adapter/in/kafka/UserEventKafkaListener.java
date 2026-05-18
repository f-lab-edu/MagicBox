package kr.magicbox.subscribe.adapter.in.kafka;

import kr.magicbox.subscribe.adapter.in.kafka.event.UserBannedEvent;
import kr.magicbox.subscribe.adapter.in.kafka.event.UserWithdrawnEvent;
import kr.magicbox.subscribe.application.dto.command.HandleUserRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleUserRevokedUseCase;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventKafkaListener {
    private final HandleUserRevokedUseCase handleUserRevokedUseCase;

    @RetryableTopic
    @KafkaListener(topics = "outbox.event.user-withdrawn", groupId = "subscribe-service")
    public void handleUserWithdrawnEvent(ConsumerRecord<String, UserWithdrawnEvent> consumerRecord) {
        UserWithdrawnEvent event = consumerRecord.value();
        handleUserRevokedUseCase.handleUserRevoked(
                HandleUserRevokedCommand.of(SubscriberId.of(event.userId().value()))
        );
    }

    @RetryableTopic
    @KafkaListener(topics = "outbox.event.user-banned", groupId = "subscribe-service")
    public void handleUserBannedEvent(ConsumerRecord<String, UserBannedEvent> consumerRecord) {
        UserBannedEvent event = consumerRecord.value();
        handleUserRevokedUseCase.handleUserRevoked(
                HandleUserRevokedCommand.of(SubscriberId.of(event.userId().value()))
        );
    }
}
