package kr.magicbox.notification.adapter.in.kafka;

import kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.notification.adapter.in.kafka.event.UserBannedEvent;
import kr.magicbox.notification.adapter.in.kafka.event.UserDuplicateLoggedInEvent;
import kr.magicbox.notification.adapter.in.kafka.event.UserSignupEvent;
import kr.magicbox.notification.adapter.in.kafka.event.UserUnbannedEvent;
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
public class UserEventKafkaListener {

    private final SaveNotificationUseCase saveNotificationUseCase;
    private final NotificationInboxJpaRepository notificationInboxJpaRepository;

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-signup", groupId = "notification-service")
    public void handleUserSignup(ConsumerRecord<String, UserSignupEvent> consumerRecord) {
        UserSignupEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.USER_SIGNUP));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-banned", groupId = "notification-service")
    public void handleUserBanned(ConsumerRecord<String, UserBannedEvent> consumerRecord) {
        UserBannedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.USER_BANNED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-unbanned", groupId = "notification-service")
    public void handleUserUnbanned(ConsumerRecord<String, UserUnbannedEvent> consumerRecord) {
        UserUnbannedEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.USER_UNBANNED));
    }

    @Idempotent
    @RetryableTopic(dltStrategy = DltStrategy.FAIL_ON_ERROR, dltTopicSuffix = "-dlt", exclude = {BusinessException.class})
    @KafkaListener(topics = "outbox.event.user-duplicate-logged-in", groupId = "notification-service")
    public void handleUserDuplicateLoggedIn(ConsumerRecord<String, UserDuplicateLoggedInEvent> consumerRecord) {
        UserDuplicateLoggedInEvent event = consumerRecord.value();
        saveNotificationUseCase.save(SaveNotificationCommand.of(event.userId(), NotificationType.USER_DUPLICATE_LOGGED_IN));
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        notificationInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(NotificationInboxEntity::markDeadLettered);
    }
}
