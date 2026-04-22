package kr.magicbox.subscribe.adapter.in.kafka.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.kafka.retry")
public class KafkaRetryProperties {
    private final Long intervalMs;
    private final Long maxAttempts;
}
