package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.PaymentCancelFailedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.PaymentCancelSucceededEvent;
import kr.magicbox.notification.adapter.in.kafka.event.PaymentFailedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.PaymentSucceededEvent;
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
public class PaymentEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-succeeded", groupId = "notification-service")
    public void handlePaymentSucceeded(ConsumerRecord<String, PaymentSucceededEvent> consumerRecord) {
        PaymentSucceededEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.PAYMENT_SUCCEEDED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-failed", groupId = "notification-service")
    public void handlePaymentFailed(ConsumerRecord<String, PaymentFailedEvent> consumerRecord) {
        PaymentFailedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.PAYMENT_FAILED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-cancel-succeeded", groupId = "notification-service")
    public void handlePaymentCancelSucceeded(ConsumerRecord<String, PaymentCancelSucceededEvent> consumerRecord) {
        PaymentCancelSucceededEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.PAYMENT_CANCEL_SUCCEEDED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.payment-cancel-failed", groupId = "notification-service")
    public void handlePaymentCancelFailed(ConsumerRecord<String, PaymentCancelFailedEvent> consumerRecord) {
        PaymentCancelFailedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.PAYMENT_CANCEL_FAILED));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
