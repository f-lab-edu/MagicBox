package kr.magicbox.payment.adapter.in.kafka;

import kr.magicbox.payment.adapter.in.kafka.annotation.Idempotent;
import kr.magicbox.payment.adapter.in.kafka.event.PaymentApproveCommandEvent;
import kr.magicbox.payment.adapter.in.kafka.event.PaymentCancelCommandEvent;
import kr.magicbox.payment.application.port.in.HandlePaymentApproveCommandUseCase;
import kr.magicbox.payment.application.port.in.HandlePaymentCancelCommandUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCommandKafkaListener {

    private final HandlePaymentApproveCommandUseCase handlePaymentApproveCommandUseCase;
    private final HandlePaymentCancelCommandUseCase handlePaymentCancelCommandUseCase;

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "outbox.event.payment-approve", groupId = "payment-service")
    public void handlePaymentApproveCommand(ConsumerRecord<String, PaymentApproveCommandEvent> consumerRecord) {
        log.info("[Inbox] payment-approve command 수신. key={}", consumerRecord.key());
        PaymentApproveCommandEvent event = consumerRecord.value();
        handlePaymentApproveCommandUseCase.handlePaymentApproveCommand(
                event.orderId(),
                event.customerId(),
                event.totalAmount(),
                event.items()
        );
    }

    @Idempotent
    @RetryableTopic
    @KafkaListener(topics = "outbox.event.payment-cancel", groupId = "payment-service")
    public void handlePaymentCancelCommand(ConsumerRecord<String, PaymentCancelCommandEvent> consumerRecord) {
        log.info("[Inbox] payment-cancel command 수신. key={}", consumerRecord.key());
        PaymentCancelCommandEvent event = consumerRecord.value();
        handlePaymentCancelCommandUseCase.handlePaymentCancelCommand(event.orderId(), event.customerId());
    }
}
