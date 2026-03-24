package kr.magicbox.auth.adapter.out.communication.kafka.listener;

import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogoutDlqEventProducerListener implements ProducerListener<String, UserLoggedOutEvent> {

    @Override
    public void onError(ProducerRecord<String, UserLoggedOutEvent> record, RecordMetadata metadata, Exception ex) {
        log.error("DLQ 이벤트 발행 실패 - userId: {}, topic: {}, error: {}", record.key(), record.topic(), ex.getMessage());
    }
}