package kr.magicbox.order.adapter.in.scheduler;

import kr.magicbox.order.adapter.in.scheduler.properties.AutoConfirmProperties;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.redisson.RedissonLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT10M")
@EnableConfigurationProperties(AutoConfirmProperties.class)
@Configuration
public class SchedulerConfiguration {

    @Bean
    public LockProvider lockProvider(RedissonClient redissonClient) {
        return new RedissonLockProvider(redissonClient);
    }
}
