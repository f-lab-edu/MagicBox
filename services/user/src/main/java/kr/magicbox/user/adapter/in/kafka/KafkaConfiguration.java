package kr.magicbox.user.adapter.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;

    @Bean
    public CommonErrorHandler errorHandler(KafkaTemplate<?, ?> kafkaTemplate) {
        KafkaProperties.Retry.Topic retryTopic = kafkaProperties.getRetry().getTopic();
        ExponentialBackOff backOff = new ExponentialBackOff(
                retryTopic.getBackoff().getDelay().toMillis(),
                retryTopic.getBackoff().getMultiplier()
        );
        backOff.setMaxAttempts(retryTopic.getAttempts());

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (ConsumerRecord<?, ?> record, Exception ex) -> {
                    log.error("[DLT] 메시지 처리 실패, DLT 전송합니다. topic={}, offset={}, exception={}", record.topic(), record.offset(), ex.getMessage());
                    return new TopicPartition(record.topic() + "-dlt", record.partition());
                });
        return new DefaultErrorHandler(recoverer, backOff);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConsumerFactory<Object, Object> consumerFactory,
            CommonErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}
