package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.ReleaseCreatedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.ReleaseUpdatedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.StockReserveFailedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.StockReserveSucceededEvent;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxEntity;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationTemplateEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationInboxJpaRepository;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationTemplateJpaRepository;
import kr.magicbox.notification.application.dto.command.SaveNotificationCommand;
import kr.magicbox.notification.application.port.in.SaveNotificationUseCase;
import kr.magicbox.notification.application.port.out.FcmTopicPort;
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
public class ReleaseEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;
    private final NotificationTemplateJpaRepository notificationTemplateJpaRepository;
    private final FcmTopicPort fcmTopicPort;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.release-created", groupId = "notification-service")
    public void handleReleaseCreated(ConsumerRecord<String, ReleaseCreatedEvent> consumerRecord) {
        ReleaseCreatedEvent event = consumerRecord.value();
        String topic = "creator_" + event.creatorId();
        notificationTemplateJpaRepository.findByType(NotificationType.RELEASE_CREATED)
                .ifPresent((NotificationTemplateEntity template) ->
                        fcmTopicPort.sendToTopic(topic, template.getTitle(), template.getBody()));
        log.info("[Notification] 릴리즈 등록 FCM Topic 전송. creatorId={}, releaseId={}", event.creatorId(), event.releaseId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.release-updated", groupId = "notification-service")
    public void handleReleaseUpdated(ConsumerRecord<String, ReleaseUpdatedEvent> consumerRecord) {
        ReleaseUpdatedEvent event = consumerRecord.value();
        String topic = "creator_" + event.creatorId();
        notificationTemplateJpaRepository.findByType(NotificationType.RELEASE_UPDATED)
                .ifPresent((NotificationTemplateEntity template) ->
                        fcmTopicPort.sendToTopic(topic, template.getTitle(), template.getBody()));
        log.info("[Notification] 릴리즈 수정 FCM Topic 전송. creatorId={}, releaseId={}", event.creatorId(), event.releaseId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.stock-reserve-succeeded", groupId = "notification-service")
    public void handleStockReserveSucceeded(ConsumerRecord<String, StockReserveSucceededEvent> consumerRecord) {
        StockReserveSucceededEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.STOCK_RESERVE_SUCCEEDED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.stock-reserve-failed", groupId = "notification-service")
    public void handleStockReserveFailed(ConsumerRecord<String, StockReserveFailedEvent> consumerRecord) {
        StockReserveFailedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.STOCK_RESERVE_FAILED));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
