package kr.magicbox.auth.adapter.out.communication.kafka;

import kr.magicbox.auth.adapter.out.communication.kafka.properties.KafkaTopicProperties;
import kr.magicbox.auth.application.port.out.LogoutEventPublisherPort;
import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutEventKafkaAdapter implements LogoutEventPublisherPort {

    private final KafkaTemplate<String, UserLoggedOutEvent> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void publish(UserLoggedOutEvent event) {
        kafkaTemplate.send(kafkaTopicProperties.getUserLoggedOut(), event.userId().toString(), event);
    }
}