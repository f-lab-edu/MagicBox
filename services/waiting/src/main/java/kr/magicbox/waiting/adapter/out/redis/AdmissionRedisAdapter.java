package kr.magicbox.waiting.adapter.out.redis;

import kr.magicbox.waiting.application.port.out.AdmissionQueuePort;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AdmissionRedisAdapter implements AdmissionQueuePort {

    private final ReactiveStringRedisTemplate redisTemplate;

    private static final String ACTIVE_KEY_PREFIX = "waiting:active:";
    private static final String BATCH_SIZE_KEY_PREFIX = "waiting:batch_size:";

    private String activeKey(ReleaseId releaseId) {
        return ACTIVE_KEY_PREFIX + releaseId.value();
    }

    private String batchSizeKey(ReleaseId releaseId) {
        return BATCH_SIZE_KEY_PREFIX + releaseId.value();
    }

    @Override
    public Mono<Void> activate(ReleaseId releaseId, int count) {
        return redisTemplate.opsForValue()
                .increment(activeKey(releaseId), count)
                .then();
    }

    @Override
    public Mono<Void> deactivate(ReleaseId releaseId) {
        return redisTemplate.opsForValue()
                .decrement(activeKey(releaseId))
                .then();
    }

    @Override
    public Mono<Long> getActiveCount(ReleaseId releaseId) {
        return redisTemplate.opsForValue()
                .get(activeKey(releaseId))
                .map(Long::parseLong)
                .defaultIfEmpty(0L);
    }

    @Override
    public Mono<Void> clear(ReleaseId releaseId) {
        return redisTemplate.delete(activeKey(releaseId), batchSizeKey(releaseId)).then();
    }

    public Mono<Void> saveBatchSize(ReleaseId releaseId, int batchSize) {
        return redisTemplate.opsForValue()
                .set(batchSizeKey(releaseId), String.valueOf(batchSize))
                .then();
    }

    public Mono<Integer> getBatchSize(ReleaseId releaseId) {
        return redisTemplate.opsForValue()
                .get(batchSizeKey(releaseId))
                .map(Integer::parseInt)
                .defaultIfEmpty(0);
    }
}
