package kr.magicbox.media.adapter.in.scheduler;

import kr.magicbox.media.application.port.in.CleanupInactiveMediaUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InactiveMediaCleanupScheduler {

    private final CleanupInactiveMediaUseCase cleanupInactiveMediaUseCase;
    private final RedissonClient redissonClient;
    private final SchedulerProperties schedulerProperties;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupInactiveMedia() {
        RLock lock = redissonClient.getLock(schedulerProperties.getCleanupInactiveMediaLockKey());
        if (!lock.tryLock()) {
            return;
        }
        try {
            log.info("[Scheduler] 고아 미디어 정리 시작");
            cleanupInactiveMediaUseCase.cleanupInactiveMedia();
            log.info("[Scheduler] 고아 미디어 정리 완료");
        } finally {
            lock.unlock();
        }
    }
}
