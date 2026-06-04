package kr.magicbox.waiting.adapter.out.redis;

import kr.magicbox.waiting.application.port.out.WaitingQueuePort;
import kr.magicbox.waiting.global.properties.WaitingProperties;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WaitingRedisAdapter implements WaitingQueuePort {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final WaitingProperties waitingProperties;


    private static final RedisScript<Long> ZADD_NX = RedisScript.of(
            "return redis.call('ZADD', KEYS[1], 'NX', ARGV[1], ARGV[2])", Long.class);

    private String queueKey(ReleaseId releaseId) {
        return waitingProperties.getQueueKeyPrefix() + releaseId.value();
    }

    @Override
    public Mono<Boolean> enqueue(ReleaseId releaseId, UserId userId, long score) {
        return redisTemplate
                .execute(ZADD_NX, List.of(queueKey(releaseId)), String.valueOf(score), String.valueOf(userId.value()))
                .next()
                .map(added -> added > 0)
                .defaultIfEmpty(false)
                .flatMap(added -> {
                    if (!added) return Mono.just(false);
                    return redisTemplate.opsForValue()
                            .set(waitingProperties.getUserReleaseKeyPrefix() + userId.value(),
                                    String.valueOf(releaseId.value()),
                                    Duration.ofSeconds(waitingProperties.getPurchaseTokenTtlSeconds() * 2))
                            .thenReturn(true);
                });
    }

    @Override
    public Mono<Long> getRank(ReleaseId releaseId, UserId userId) {
        return redisTemplate.opsForZSet()
                .rank(queueKey(releaseId), String.valueOf(userId.value()));
    }

    @Override
    public Mono<Long> getQueueSize(ReleaseId releaseId) {
        return redisTemplate.opsForZSet()
                .size(queueKey(releaseId));
    }

    @Override
    public Mono<Void> dequeue(ReleaseId releaseId, UserId userId) {
        return redisTemplate.opsForZSet()
                .remove(queueKey(releaseId), String.valueOf(userId.value()))
                .then();
    }

    @Override
    public Flux<UserId> peekFront(ReleaseId releaseId, int count) {
        return redisTemplate.opsForZSet()
                .range(queueKey(releaseId), Range.closed(0L, (long) count - 1))
                .map(member -> UserId.of(Long.parseLong(member)));
    }

    @Override
    public Mono<Long> removeFront(ReleaseId releaseId, int count) {
        return redisTemplate.opsForZSet()
                .removeRange(queueKey(releaseId), Range.closed(0L, (long) count - 1));
    }

    @Override
    public Mono<Void> clear(ReleaseId releaseId) {
        return redisTemplate.delete(queueKey(releaseId)).then();
    }

    @Override
    public Mono<ReleaseId> findReleaseIdByUserId(UserId userId) {
        return redisTemplate.opsForValue()
                .get(waitingProperties.getUserReleaseKeyPrefix() + userId.value())
                .map(releaseIdStr -> ReleaseId.of(Long.parseLong(releaseIdStr)));
    }
}
