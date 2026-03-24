package kr.magicbox.auth.adapter.out.communication.kafka.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {

    private final String authLoggedOut;
}