package kr.magicbox.payment.application.service;

import kr.magicbox.payment.application.dto.result.PaymentResult;
import kr.magicbox.payment.domain.aggregate.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentResultMapper {

    public PaymentResult toResult(Payment payment) {
        return PaymentResult.builder()
                .paymentId(payment.getId().value())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .status(payment.getStatus())
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())
                .paymentKey(payment.getPaymentKey())
                .paymentMethod(payment.getPaymentMethod())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .paymentLines(payment.getPaymentLines().stream()
                        .map(line -> PaymentResult.PaymentLineResult.builder()
                                .paymentLineId(line.getId() != null ? line.getId().value() : null)
                                .orderLineId(line.getOrderLineId())
                                .sellerId(line.getSellerId())
                                .amount(line.getAmount())
                                .build())
                        .toList())
                .build();
    }
}
