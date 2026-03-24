package kr.magicbox.auth.adapter.out.communication.kafka;

import kr.magicbox.auth.adapter.out.communication.kafka.properties.KafkaTopicProperties;
import kr.magicbox.auth.adapter.out.communication.kafka.qualifier.LogoutKafka;
import kr.magicbox.auth.application.port.out.LogoutEventPublisherPort;
import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutEventKafkaAdapter implements LogoutEventPublisherPort {

    @LogoutKafka
    private final KafkaTemplate<String, UserLoggedOutEvent> logoutEventKafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void publish(UserLoggedOutEvent event) {
        logoutEventKafkaTemplate.send(kafkaTopicProperties.getAuthLoggedOut(), event.userId().toString(), event);
    }
}