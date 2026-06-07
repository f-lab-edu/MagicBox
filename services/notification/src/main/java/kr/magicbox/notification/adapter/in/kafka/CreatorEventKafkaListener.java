package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.CreatorCertificationApprovedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.CreatorCertificationRejectedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.CreatorRevokedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.CreatorUnbannedEvent;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationInboxJpaRepository;
import kr.magicbox.notification.application.dto.command.SaveNotificationCommand;
import kr.magicbox.notification.application.port.in.SaveNotificationUseCase;
import kr.magicbox.notification.domain.enums.NotificationType;
import kr.magicbox.notification.global.exception.BusinessException;
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
public class CreatorEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.creator-certification-approved", groupId = "notification-service")
    public void handleCertificationApproved(ConsumerRecord<String, CreatorCertificationApprovedEvent> consumerRecord) {
        CreatorCertificationApprovedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.CREATOR_CERTIFICATION_APPROVED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.creator-certification-rejected", groupId = "notification-service")
    public void handleCertificationRejected(ConsumerRecord<String, CreatorCertificationRejectedEvent> consumerRecord) {
        CreatorCertificationRejectedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.CREATOR_CERTIFICATION_REJECTED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.creator-revoked", groupId = "notification-service")
    public void handleCreatorRevoked(ConsumerRecord<String, CreatorRevokedEvent> consumerRecord) {
        CreatorRevokedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.CREATOR_REVOKED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.creator-unbanned", groupId = "notification-service")
    public void handleCreatorUnbanned(ConsumerRecord<String, CreatorUnbannedEvent> consumerRecord) {
        CreatorUnbannedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.CREATOR_UNBANNED));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
