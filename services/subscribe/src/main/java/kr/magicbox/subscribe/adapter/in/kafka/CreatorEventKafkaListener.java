package kr.magicbox.subscribe.adapter.in.kafka;

import kr.magicbox.subscribe.adapter.in.kafka.event.CreatorRevokedEvent;
import kr.magicbox.subscribe.application.dto.command.HandleCreatorRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleCreatorRevokedUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorEventKafkaListener {
    private final HandleCreatorRevokedUseCase handleCreatorRevokedUseCase;

    @KafkaListener(topics = "outbox.event.creator-revoked", groupId = "subscribe-service")
    public void handleCreatorRevokedEvent(ConsumerRecord<String, CreatorRevokedEvent> consumerRecord) {
        CreatorRevokedEvent event = consumerRecord.value();
        handleCreatorRevokedUseCase.handleCreatorRevoked(HandleCreatorRevokedCommand.of(event.creatorId()));
    }
}
