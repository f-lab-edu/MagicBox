package kr.magicbox.orchestrator.adapter.in.kafka.aop;

import kr.magicbox.orchestrator.adapter.in.kafka.properties.InboxProperties;
import kr.magicbox.orchestrator.adapter.out.persistence.entity.OrchestratorInboxEntity;
import kr.magicbox.orchestrator.adapter.out.persistence.entity.OrchestratorInboxStatus;
import kr.magicbox.orchestrator.adapter.out.persistence.repository.OrchestratorInboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.RecordComponent;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final OrchestratorInboxRepository orchestratorInboxRepository;
    private final TransactionTemplate transactionTemplate;
    private final InboxProperties inboxProperties;

    @Around("@annotation(kr.magicbox.orchestrator.adapter.in.kafka.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) {
        ConsumerRecord<String, ?> consumerRecord = extractRecord(pjp);
        Long eventId = Long.parseLong(consumerRecord.key());
        Instant occurredAt = extractOccurredAt(consumerRecord.value());

        if (isTooOld(occurredAt)) {
            log.warn("[Inbox] 만료된 메시지 폐기. eventId={}, occurredAt={}", eventId, occurredAt);
            return null;
        }

        return transactionTemplate.execute(status -> {
            if (orchestratorInboxRepository.existsByEventId(eventId)) {
                log.warn("[Inbox] 중복 메시지 폐기. eventId={}", eventId);
                return null;
            }
            OrchestratorInboxEntity inbox = orchestratorInboxRepository.save(OrchestratorInboxEntity.builder()
                    .eventId(eventId)
                    .topic(consumerRecord.topic())
                    .partition(consumerRecord.partition())
                    .offset(consumerRecord.offset())
                    .status(OrchestratorInboxStatus.PENDING)
                    .occurredAt(occurredAt)
                    .build());
            try {
                pjp.proceed();
            } catch (Throwable e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
            inbox.markProcessed();
            return null;
        });
    }

    private boolean isTooOld(Instant occurredAt) {
        return occurredAt.isBefore(Instant.now().minus(inboxProperties.getMaxEventAgeMinutes(), ChronoUnit.MINUTES));
    }

    private Instant extractOccurredAt(Object payload) {
        if (payload == null) {
            return Instant.now();
        }
        try {
            for (RecordComponent component : payload.getClass().getRecordComponents()) {
                if (component.getName().equals("occurredAt")) {
                    Object value = component.getAccessor().invoke(payload);
                    if (value instanceof Instant instant) {
                        return instant;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("[Inbox] occurredAt 추출 실패, 현재 시각으로 대체. payload={}", payload.getClass().getSimpleName());
        }
        return Instant.now();
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
