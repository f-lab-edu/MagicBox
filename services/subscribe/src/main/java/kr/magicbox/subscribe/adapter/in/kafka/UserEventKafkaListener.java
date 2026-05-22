package kr.magicbox.subscribe.adapter.in.kafka;

import kr.magicbox.subscribe.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.subscribe.adapter.in.kafka.event.UserBannedEvent;
import kr.magicbox.subscribe.adapter.in.kafka.event.UserWithdrawnEvent;
import kr.magicbox.subscribe.application.dto.command.HandleUserRevokedCommand;
import kr.magicbox.subscribe.adapter.out.persistence.repository.SubscribeInboxRepository;
import kr.magicbox.subscribe.application.port.in.HandleUserRevokedUseCase;
import kr.magicbox.subscribe.global.exception.BusinessException;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventKafkaListener {
    private final HandleUserRevokedUseCase handleUserRevokedUseCase;
    private final SubscribeInboxRepository subscribeInboxRepository;

    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-withdrawn", groupId = "subscribe-service")
    public void handleUserWithdrawnEvent(ConsumerRecord<String, UserWithdrawnEvent> consumerRecord) {
        UserWithdrawnEvent event = consumerRecord.value();
        handleUserRevokedUseCase.handleUserRevoked(
                HandleUserRevokedCommand.of(SubscriberId.of(event.userId().value()))
        );
    }

    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-banned", groupId = "subscribe-service")
    public void handleUserBannedEvent(ConsumerRecord<String, UserBannedEvent> consumerRecord) {
        UserBannedEvent event = consumerRecord.value();
        handleUserRevokedUseCase.handleUserRevoked(
                HandleUserRevokedCommand.of(SubscriberId.of(event.userId().value()))
        );
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        subscribeInboxRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(inbox -> inbox.markDeadLettered());
    }
}
