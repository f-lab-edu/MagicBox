package kr.magicbox.settlement.adapter.in.kafka;

import kr.magicbox.settlement.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.settlement.adapter.in.kafka.event.SettlementReadyCommandEvent;
import kr.magicbox.settlement.adapter.in.kafka.event.SettlementSettleCommandEvent;
import kr.magicbox.settlement.adapter.out.persistence.repository.SettlementInboxJpaRepository;
import kr.magicbox.settlement.application.port.in.HandleSettlementReadyCommandUseCase;
import kr.magicbox.settlement.application.port.in.HandleSettlementSettleCommandUseCase;
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
public class SettlementCommandKafkaListener {

    private final HandleSettlementReadyCommandUseCase handleSettlementReadyCommandUseCase;
    private final HandleSettlementSettleCommandUseCase handleSettlementSettleCommandUseCase;
    private final SettlementInboxJpaRepository settlementInboxJpaRepository;

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "command.settlement-ready", groupId = "settlement-service")
    public void handleSettlementReadyCommand(ConsumerRecord<String, SettlementReadyCommandEvent> consumerRecord) {
        log.info("[Inbox] settlement-ready command 수신. eventId={}", consumerRecord.key());
        SettlementReadyCommandEvent event = consumerRecord.value();
        handleSettlementReadyCommandUseCase.handleSettlementReadyCommand(event.orderId(), event.orderLineId());
    }

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "command.settlement-settle", groupId = "settlement-service")
    public void handleSettlementSettleCommand(ConsumerRecord<String, SettlementSettleCommandEvent> consumerRecord) {
        log.info("[Inbox] settlement-settle command 수신. eventId={}", consumerRecord.key());
        SettlementSettleCommandEvent event = consumerRecord.value();
        handleSettlementSettleCommandUseCase.handleSettlementSettleCommand(
                event.orderId(), event.orderLineId(), event.sellerId(), event.grossAmount());
    }

    @DltHandler
    public void handleDlt(ConsumerRecord<String, ?> consumerRecord) {
        log.error("[Inbox] DLT 전환. topic={}, partition={}, offset={}", consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset());
        settlementInboxJpaRepository.findByTopicAndPartitionAndOffset(consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset())
                .ifPresent(inbox -> inbox.markDeadLettered());
    }
}
