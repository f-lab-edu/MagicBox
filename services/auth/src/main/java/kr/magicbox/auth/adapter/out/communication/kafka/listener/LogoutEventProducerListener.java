package kr.magicbox.auth.adapter.out.communication.kafka.listener;

import kr.magicbox.auth.adapter.out.communication.kafka.properties.KafkaTopicProperties;
import kr.magicbox.auth.adapter.out.communication.kafka.qualifier.LogoutDlqKafka;
import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutEventProducerListener implements ProducerListener<String, UserLoggedOutEvent> {

    @LogoutDlqKafka
    private final KafkaTemplate<String, UserLoggedOutEvent> logoutEventDlqKafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void onError(ProducerRecord<String, UserLoggedOutEvent> record, RecordMetadata metadata, Exception ex) {
        log.error("Kafka 이벤트 발행 실패 - userId: {}, error: {}", record.key(), ex.getMessage());
        logoutEventDlqKafkaTemplate.send(kafkaTopicProperties.getAuthLoggedOutDlq(), record.key(), record.value());
    }
}