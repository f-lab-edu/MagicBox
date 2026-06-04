package kr.magicbox.waiting.adapter.in.scheduler;

import jakarta.annotation.PostConstruct;
import kr.magicbox.waiting.domain.exception.ActiveReleaseRedisSyncException;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 현재 대기열이 활성화된 릴리즈 목록을 관리한다.
 * 대기열 진입 시 등록, 매진/대기열 소진 시 제거.
 *
 * JVM Set을 primary로 사용하고, Redis Set("active_releases")에 동기화하여
 * 서버 재시작 시 복구한다. Redis 장애 시 로그만 남기고 앱 시작을 허용한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ActiveReleaseRegistry {

    private static final String ACTIVE_RELEASES_KEY = "active_releases";
    private static final int RETRY_MAX_ATTEMPTS = 3;
    private static final Duration RETRY_BACKOFF = Duration.ofMillis(100);

    private final Set<ReleaseId> activeReleaseIds = ConcurrentHashMap.newKeySet();
    private final ReactiveStringRedisTemplate redisTemplate;

    @PostConstruct
    public void restore() {
        redisTemplate.opsForSet()
                .members(ACTIVE_RELEASES_KEY)
                .map(value -> ReleaseId.of(Long.parseLong(value)))
                .collectList()
                .doOnSuccess(ids -> {
                    if (ids == null) return;
                    activeReleaseIds.addAll(ids);
                    log.info("ActiveReleaseRegistry 복구 완료: {}개", ids.size());
                })
                .doOnError(e -> log.warn("ActiveReleaseRegistry Redis 복구 실패 - 빈 상태로 시작", e))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    public void register(ReleaseId releaseId) {
        activeReleaseIds.add(releaseId);
        redisTemplate.opsForSet()
                .add(ACTIVE_RELEASES_KEY, String.valueOf(releaseId.value()))
                .retryWhen(Retry.backoff(RETRY_MAX_ATTEMPTS, RETRY_BACKOFF))
                .subscribe(
                        null,
                        e -> log.error("[ActiveReleaseRedisSyncException] active_releases 등록 최종 실패 releaseId={}",
                                releaseId.value(),
                                new ActiveReleaseRedisSyncException("active_releases 등록 실패 releaseId=" + releaseId.value(), e))
                );
    }

    public void deregister(ReleaseId releaseId) {
        activeReleaseIds.remove(releaseId);
        redisTemplate.opsForSet()
                .remove(ACTIVE_RELEASES_KEY, String.valueOf(releaseId.value()))
                .retryWhen(Retry.backoff(RETRY_MAX_ATTEMPTS, RETRY_BACKOFF))
                .subscribe(
                        null,
                        e -> log.error("[ActiveReleaseRedisSyncException] active_releases 제거 최종 실패 releaseId={}",
                                releaseId.value(),
                                new ActiveReleaseRedisSyncException("active_releases 제거 실패 releaseId=" + releaseId.value(), e))
                );
    }

    public Set<ReleaseId> getActiveReleaseIds() {
        return Collections.unmodifiableSet(activeReleaseIds);
    }
}
