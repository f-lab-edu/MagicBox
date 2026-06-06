package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.GeneralGoodsCreatedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.GeneralGoodsUpdatedEvent;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxEntity;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationTemplateEntity;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationInboxJpaRepository;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationTemplateJpaRepository;
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
public class GeneralGoodsEventKafkaListener {

    private final NotificationInboxJpaRepository notificationInboxJpaRepository;
    private final NotificationTemplateJpaRepository notificationTemplateJpaRepository;
    private final FcmTopicPort fcmTopicPort;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.general-goods-created", groupId = "notification-service")
    public void handleGeneralGoodsCreated(ConsumerRecord<String, GeneralGoodsCreatedEvent> consumerRecord) {
        GeneralGoodsCreatedEvent event = consumerRecord.value();
        String topic = "creator_" + event.creatorId();
        notificationTemplateJpaRepository.findByType(NotificationType.GENERAL_GOODS_CREATED)
                .ifPresent((NotificationTemplateEntity template) ->
                        fcmTopicPort.sendToTopic(topic, template.getTitle(), template.getBody()));
        log.info("[Notification] 일반 상품 등록 FCM Topic 전송. creatorId={}, generalGoodsId={}", event.creatorId(), event.generalGoodsId());
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.general-goods-updated", groupId = "notification-service")
    public void handleGeneralGoodsUpdated(ConsumerRecord<String, GeneralGoodsUpdatedEvent> consumerRecord) {
        GeneralGoodsUpdatedEvent event = consumerRecord.value();
        String topic = "creator_" + event.creatorId();
        notificationTemplateJpaRepository.findByType(NotificationType.GENERAL_GOODS_UPDATED)
                .ifPresent((NotificationTemplateEntity template) ->
                        fcmTopicPort.sendToTopic(topic, template.getTitle(), template.getBody()));
        log.info("[Notification] 일반 상품 수정 FCM Topic 전송. creatorId={}, generalGoodsId={}", event.creatorId(), event.generalGoodsId());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
