package kr.magicbox.waiting.adapter.in.kafka;

import kr.magicbox.waiting.application.port.in.DequeueUseCase;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEventKafkaListener {

    private static final long GRACE_PERIOD_SECONDS = 30;
    private static final String DISCONNECTED_KEY_PREFIX = "sse_disconnected:";

    private final ReactiveStringRedisTemplate redisTemplate;
    private final DequeueUseCase dequeueUseCase;

    @KafkaListener(topics = "sse.connected", groupId = "waiting-service")
    public void handleConnected(ConsumerRecord<String, String> record) {
        Long userId = Long.parseLong(record.key());
        log.debug("SSE 연결 이벤트 수신 userId={}", userId);
        redisTemplate.delete(DISCONNECTED_KEY_PREFIX + userId)
                .subscribe();
    }

    @KafkaListener(topics = "sse.disconnected", groupId = "waiting-service")
    public void handleDisconnected(ConsumerRecord<String, String> record) {
        Long userId = Long.parseLong(record.key());
        log.debug("SSE 끊김 이벤트 수신 userId={} grace period {}초 시작", userId, GRACE_PERIOD_SECONDS);
        redisTemplate.opsForValue()
                .set(DISCONNECTED_KEY_PREFIX + userId, "1", Duration.ofSeconds(GRACE_PERIOD_SECONDS))
                .then(reactor.core.publisher.Mono.delay(Duration.ofSeconds(GRACE_PERIOD_SECONDS)))
                .flatMap(tick -> redisTemplate.hasKey(DISCONNECTED_KEY_PREFIX + userId))
                .flatMap(expired -> {
                    if (!expired) return reactor.core.publisher.Mono.empty();
                    return dequeueUseCase.dequeue(UserId.of(userId));
                })
                .subscribe();
    }
}
