package kr.magicbox.auth.adapter.out.communication.kafka.configuration;

import kr.magicbox.auth.adapter.out.communication.kafka.listener.LogoutDlqEventProducerListener;
import kr.magicbox.auth.adapter.out.communication.kafka.listener.LogoutEventProducerListener;
import kr.magicbox.auth.adapter.out.communication.kafka.qualifier.LogoutDlqKafka;
import kr.magicbox.auth.adapter.out.communication.kafka.qualifier.LogoutKafka;
import kr.magicbox.auth.domain.event.UserLoggedOutEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

    @Bean
    @LogoutKafka
    public KafkaTemplate<String, UserLoggedOutEvent> logoutEventKafkaTemplate(
            ProducerFactory<String, UserLoggedOutEvent> producerFactory,
            LogoutEventProducerListener logoutEventProducerListener) {
        KafkaTemplate<String, UserLoggedOutEvent> template = new KafkaTemplate<>(producerFactory);
        template.setProducerListener(logoutEventProducerListener);
        return template;
    }

    @Bean
    @LogoutDlqKafka
    public KafkaTemplate<String, UserLoggedOutEvent> logoutEventDlqKafkaTemplate(
            ProducerFactory<String, UserLoggedOutEvent> producerFactory,
            LogoutDlqEventProducerListener logoutDlqEventProducerListener) {
        KafkaTemplate<String, UserLoggedOutEvent> template = new KafkaTemplate<>(producerFactory);
        template.setProducerListener(logoutDlqEventProducerListener);
        return template;
    }
}