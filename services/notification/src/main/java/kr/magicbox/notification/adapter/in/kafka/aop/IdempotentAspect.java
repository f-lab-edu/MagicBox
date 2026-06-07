package kr.magicbox.notification.adapter.in.kafka.aop;

import kr.magicbox.notification.adapter.in.kafka.event.InboxEvent;
import kr.magicbox.notification.adapter.in.kafka.properties.InboxProperties;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxEntity;
import kr.magicbox.notification.adapter.out.persistence.entity.NotificationInboxStatus;
import kr.magicbox.notification.adapter.out.persistence.repository.NotificationInboxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final NotificationInboxJpaRepository notificationInboxJpaRepository;
    private final TransactionTemplate transactionTemplate;
    private final InboxProperties inboxProperties;

    @Around("@annotation(kr.magicbox.notification.adapter.in.kafka.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) {
        ConsumerRecord<String, ?> consumerRecord = extractRecord(pjp);
        String key = consumerRecord.key();
        InboxEvent event = (InboxEvent) consumerRecord.value();
        Instant occurredAt = event.occurredAt();

        if (isTooOld(occurredAt)) {
            log.warn("[Inbox] 만료된 메시지 DEAD_LETTERED 처리. key={}, occurredAt={}", key, occurredAt);
            transactionTemplate.executeWithoutResult(status ->
                    notificationInboxJpaRepository.save(NotificationInboxEntity.builder()
                            .key(key)
                            .topic(consumerRecord.topic())
                            .partition(consumerRecord.partition())
                            .offset(consumerRecord.offset())
                            .status(NotificationInboxStatus.DEAD_LETTERED)
                            .occurredAt(occurredAt)
                            .build())
            );
            return null;
        }

        return transactionTemplate.execute(status -> {
            if (notificationInboxJpaRepository.existsByKey(key)) {
                log.warn("[Inbox] 중복 메시지 폐기. key={}", key);
                return null;
            }
            NotificationInboxEntity inbox = notificationInboxJpaRepository.save(NotificationInboxEntity.builder()
                    .key(key)
                    .topic(consumerRecord.topic())
                    .partition(consumerRecord.partition())
                    .offset(consumerRecord.offset())
                    .status(NotificationInboxStatus.PENDING)
                    .occurredAt(occurredAt)
                    .build());
            try {
                pjp.proceed();
            } catch (Error e) {
                throw e;
            } catch (RuntimeException e) {
                status.setRollbackOnly();
                throw e;
            } catch (Throwable e) {
                status.setRollbackOnly();
                throw new IllegalStateException(e);
            }
            inbox.markProcessed();
            return null;
        });
    }

    private boolean isTooOld(Instant occurredAt) {
        return occurredAt.isBefore(Instant.now().minus(inboxProperties.getMaxEventAgeMinutes(), ChronoUnit.MINUTES));
    }

    @SuppressWarnings("unchecked")
    private ConsumerRecord<String, ?> extractRecord(ProceedingJoinPoint pjp) {
        return Arrays.stream(pjp.getArgs())
                .filter(ConsumerRecord.class::isInstance)
                .map(arg -> (ConsumerRecord<String, ?>) arg)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("@Idempotent 메서드에 ConsumerRecord 파라미터가 없습니다."));
    }
}
