package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderAutoConfirmedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderCancelEvent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderConfirmedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderDeliveredEvent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderPrepareEvent;
import kr.magicbox.notification.adapter.in.kafka.event.OrderPurchaseConfirmedEvent;
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
public class OrderEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-prepare", groupId = "notification-service")
    public void handleOrderPrepare(ConsumerRecord<String, OrderPrepareEvent> consumerRecord) {
        OrderPrepareEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.ORDER_PREPARE));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-confirmed", groupId = "notification-service")
    public void handleOrderConfirmed(ConsumerRecord<String, OrderConfirmedEvent> consumerRecord) {
        OrderConfirmedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.ORDER_CONFIRMED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-delivered", groupId = "notification-service")
    public void handleOrderDelivered(ConsumerRecord<String, OrderDeliveredEvent> consumerRecord) {
        OrderDeliveredEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.ORDER_DELIVERED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-cancel", groupId = "notification-service")
    public void handleOrderCancel(ConsumerRecord<String, OrderCancelEvent> consumerRecord) {
        OrderCancelEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.ORDER_CANCEL));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-purchase-confirmed", groupId = "notification-service")
    public void handleOrderPurchaseConfirmed(ConsumerRecord<String, OrderPurchaseConfirmedEvent> consumerRecord) {
        OrderPurchaseConfirmedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.sellerId(), NotificationType.ORDER_PURCHASE_CONFIRMED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.order-auto-confirmed", groupId = "notification-service")
    public void handleOrderAutoConfirmed(ConsumerRecord<String, OrderAutoConfirmedEvent> consumerRecord) {
        OrderAutoConfirmedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.customerId(), NotificationType.ORDER_AUTO_CONFIRMED));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
