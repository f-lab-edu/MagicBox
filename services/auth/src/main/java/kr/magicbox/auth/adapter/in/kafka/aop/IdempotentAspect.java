package kr.magicbox.auth.adapter.in.kafka.aop;

import kr.magicbox.auth.adapter.out.persistence.entity.AuthInboxEntity;
import kr.magicbox.auth.adapter.out.persistence.entity.AuthInboxStatus;
import kr.magicbox.auth.adapter.out.persistence.repository.AuthInboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final AuthInboxRepository authInboxRepository;
    private final TransactionTemplate transactionTemplate;

    @Around("@annotation(kr.magicbox.auth.adapter.in.kafka.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) {
        ConsumerRecord<String, ?> record = extractRecord(pjp);
        Long eventId = Long.parseLong(record.key());

        return transactionTemplate.execute(status -> {
            if (authInboxRepository.existsByEventId(eventId)) {
                log.warn("[Inbox] 중복 메시지 폐기. eventId={}", eventId);
                return null;
            }
            AuthInboxEntity inbox = authInboxRepository.save(AuthInboxEntity.builder()
                    .eventId(eventId)
                    .topic(record.topic())
                    .partition(record.partition())
                    .offset(record.offset())
                    .status(AuthInboxStatus.PENDING)
                    .build());
            try {
                pjp.proceed();
            }
            catch (Throwable e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
            inbox.markProcessed();
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    private ConsumerRecord<String, ?> extractRecord(ProceedingJoinPoint pjp) {
        return Arrays.stream(pjp.getArgs())
                .filter(arg -> arg instanceof ConsumerRecord)
                .map(arg -> (ConsumerRecord<String, ?>) arg)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("@Idempotent 메서드에 ConsumerRecord 파라미터가 없습니다."));
    }
}
