package kr.magicbox.settlement.adapter.in.kafka;

import kr.magicbox.settlement.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.settlement.adapter.in.kafka.event.PaymentCancelSucceededEvent;
import kr.magicbox.settlement.application.port.in.HandlePaymentCancelSucceededUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaListener {

    private final HandlePaymentCancelSucceededUseCase handlePaymentCancelSucceededUseCase;

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "outbox.event.payment-cancel-succeeded", groupId = "settlement-service")
    public void handlePaymentCancelSucceeded(ConsumerRecord<String, PaymentCancelSucceededEvent> consumerRecord) {
        log.info("[Inbox] payment.cancel.succeeded 이벤트 수신. eventId={}", consumerRecord.key());
        handlePaymentCancelSucceededUseCase.handlePaymentCancelSucceeded(consumerRecord.value().orderId());
    }
}
