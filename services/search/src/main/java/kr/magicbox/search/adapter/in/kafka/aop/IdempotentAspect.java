package kr.magicbox.search.adapter.in.kafka.aop;

import kr.magicbox.search.adapter.in.kafka.event.InboxEvent;
import kr.magicbox.search.adapter.in.kafka.properties.InboxProperties;
import kr.magicbox.search.adapter.out.persistence.entity.SearchInboxEntity;
import kr.magicbox.search.adapter.out.persistence.entity.SearchInboxStatus;
import kr.magicbox.search.adapter.out.persistence.repository.SearchInboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final SearchInboxRepository searchInboxRepository;
    private final TransactionalOperator transactionalOperator;
    private final InboxProperties inboxProperties;

    @Around("@annotation(kr.magicbox.search.adapter.in.kafka.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) {
        ConsumerRecord<String, ?> consumerRecord = extractRecord(pjp);
        InboxEvent event = (InboxEvent) consumerRecord.value();
        Long eventId = Long.parseLong(consumerRecord.key());
        Instant occurredAt = event.occurredAt();

        if (isTooOld(occurredAt)) {
            log.warn("[Inbox] 만료된 메시지 폐기. eventId={}, occurredAt={}", eventId, occurredAt);
            return null;
        }

        searchInboxRepository.existsByEventId(eventId)
                .flatMap(exists -> {
                    if (exists) {
                        log.warn("[Inbox] 중복 메시지 폐기. eventId={}", eventId);
                        return reactor.core.publisher.Mono.empty();
                    }
                    return searchInboxRepository.save(SearchInboxEntity.builder()
                                    .eventId(eventId)
                                    .topic(consumerRecord.topic())
                                    .partition(consumerRecord.partition())
                                    .offset(consumerRecord.offset())
                                    .status(SearchInboxStatus.PENDING)
                                    .build())
                            .flatMap(inbox -> {
                                try {
                                    pjp.proceed();
                                } catch (Throwable e) {
                                    return reactor.core.publisher.Mono.error(e);
                                }
                                return searchInboxRepository.save(inbox.markProcessed()).then();
                            });
                })
                .as(transactionalOperator::transactional)
                .subscribe();

        return null;
    }

    @SuppressWarnings("unchecked")
    private ConsumerRecord<String, ?> extractRecord(ProceedingJoinPoint pjp) {
        return Arrays.stream(pjp.getArgs())
                .filter(ConsumerRecord.class::isInstance)
                .map(arg -> (ConsumerRecord<String, ?>) arg)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("@Idempotent 메서드에 ConsumerRecord 파라미터가 없습니다."));
    }

    private boolean isTooOld(Instant occurredAt) {
        return occurredAt.isBefore(
                Instant.now().minus(inboxProperties.getMaxEventAgeMinutes(), ChronoUnit.MINUTES));
    }
}
