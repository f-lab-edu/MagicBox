package kr.magicbox.user.adapter.in.kafka.aop;

import kr.magicbox.user.adapter.out.persistence.entity.UserInboxEntity;
import kr.magicbox.user.adapter.out.persistence.entity.UserInboxStatus;
import kr.magicbox.user.adapter.out.persistence.repository.UserInboxRepository;
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

    private final UserInboxRepository userInboxRepository;
    private final TransactionTemplate transactionTemplate;

    @Around("@annotation(kr.magicbox.user.adapter.in.kafka.annotation.Idempotent)")
    public Object around(ProceedingJoinPoint pjp) {
        ConsumerRecord<String, ?> consumerRecord = extractRecord(pjp);
        Long eventId = Long.parseLong(consumerRecord.key());

        return transactionTemplate.execute(status -> {
            if (userInboxRepository.existsByEventId(eventId)) {
                log.warn("[Inbox] 중복 메시지 폐기. eventId={}", eventId);
                return null;
            }
            UserInboxEntity inbox = userInboxRepository.save(UserInboxEntity.builder()
                    .eventId(eventId)
                    .topic(consumerRecord.topic())
                    .partition(consumerRecord.partition())
                    .offset(consumerRecord.offset())
                    .status(UserInboxStatus.PENDING)
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

    @SuppressWarnings("unchecked")
    private ConsumerRecord<String, ?> extractRecord(ProceedingJoinPoint pjp) {
        return Arrays.stream(pjp.getArgs())
                .filter(ConsumerRecord.class::isInstance)
                .map(arg -> (ConsumerRecord<String, ?>) arg)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("@Idempotent 메서드에 ConsumerRecord 파라미터가 없습니다."));
    }
}