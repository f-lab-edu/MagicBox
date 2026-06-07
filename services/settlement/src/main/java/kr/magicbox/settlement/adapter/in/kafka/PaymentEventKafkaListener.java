package kr.magicbox.settlement.adapter.in.kafka;

import kr.magicbox.settlement.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.settlement.adapter.in.kafka.event.PaymentCancelSucceededEvent;
import kr.magicbox.settlement.adapter.out.persistence.repository.SettlementInboxJpaRepository;
import kr.magicbox.settlement.application.port.in.HandlePaymentCancelSucceededUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaListener {

    private final HandlePaymentCancelSucceededUseCase handlePaymentCancelSucceededUseCase;
    private final SettlementInboxJpaRepository settlementInboxJpaRepository;

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "outbox.event.payment-cancel-succeeded", groupId = "settlement-service")
    public void handlePaymentCancelSucceeded(ConsumerRecord<String, PaymentCancelSucceededEvent> consumerRecord) {
        log.info("[Inbox] payment.cancel.succeeded 이벤트 수신. key={}", consumerRecord.key());
        handlePaymentCancelSucceededUseCase.handlePaymentCancelSucceeded(consumerRecord.value().orderId());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        settlementInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(inbox -> inbox.markDeadLettered());
    }
}
