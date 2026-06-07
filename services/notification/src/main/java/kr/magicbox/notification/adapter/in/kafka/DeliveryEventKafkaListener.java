package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.DeliveryCompletedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.DeliveryStartedEvent;
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
public class DeliveryEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.delivery-started", groupId = "notification-service")
    public void handleDeliveryStarted(ConsumerRecord<String, DeliveryStartedEvent> consumerRecord) {
        DeliveryStartedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.DELIVERY_STARTED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.delivery-completed", groupId = "notification-service")
    public void handleDeliveryCompleted(ConsumerRecord<String, DeliveryCompletedEvent> consumerRecord) {
        DeliveryCompletedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.DELIVERY_COMPLETED));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
