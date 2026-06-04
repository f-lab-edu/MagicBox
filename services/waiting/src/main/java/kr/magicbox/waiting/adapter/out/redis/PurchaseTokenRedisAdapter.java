package kr.magicbox.waiting.adapter.out.redis;

import kr.magicbox.waiting.application.port.out.PurchaseTokenPort;
import kr.magicbox.waiting.global.properties.WaitingProperties;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PurchaseTokenRedisAdapter implements PurchaseTokenPort {

    private final ReactiveStringRedisTemplate redisTemplate;
    private final WaitingProperties waitingProperties;

    private String tokenKey(ReleaseId releaseId, UserId userId) {
        return waitingProperties.getTokenKeyPrefix() + releaseId.value() + ":" + userId.value();
    }

    @Override
    public Mono<String> issue(ReleaseId releaseId, UserId userId) {
        String token = UUID.randomUUID().toString();
        String key = tokenKey(releaseId, userId);
        return redisTemplate.opsForValue()
                .set(key, token, Duration.ofSeconds(waitingProperties.getPurchaseTokenTtlSeconds()))
                .thenReturn(token);
    }

    @Override
    public Mono<String> get(ReleaseId releaseId, UserId userId) {
        return redisTemplate.opsForValue().get(tokenKey(releaseId, userId));
    }

    @Override
    public Mono<Boolean> consumeIfValid(ReleaseId releaseId, UserId userId, String token) {
        String key = tokenKey(releaseId, userId);
        return redisTemplate.opsForValue()
                .get(key)
                .flatMap(stored -> {
                    if (!stored.equals(token)) return Mono.just(false);
                    return redisTemplate.delete(key).thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> invalidate(ReleaseId releaseId, UserId userId) {
        return redisTemplate.delete(tokenKey(releaseId, userId)).then();
    }
}
