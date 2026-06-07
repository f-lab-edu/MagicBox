package kr.magicbox.payment.adapter.out.persistence.mapper;

import kr.magicbox.payment.adapter.out.persistence.entity.PaymentEntity;
import kr.magicbox.payment.adapter.out.persistence.entity.PaymentLineEntity;
import kr.magicbox.payment.domain.aggregate.Payment;
import kr.magicbox.payment.domain.aggregate.PaymentLine;
import kr.magicbox.payment.domain.vo.PaymentId;
import kr.magicbox.payment.domain.vo.PaymentLineId;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .status(payment.getStatus())
                .totalAmount(payment.getTotalAmount())
                .currency(payment.getCurrency())
                .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        List<PaymentLine> lines = entity.getPaymentLines().stream()
                .map(this::toLineDomain)
                .toList();

        return Payment.reconstructBuilder()
                .id(new PaymentId(entity.getId()))
                .orderId(entity.getOrderId())
                .customerId(entity.getCustomerId())
                .status(entity.getStatus())
                .totalAmount(entity.getTotalAmount())
                .currency(entity.getCurrency())
                .paymentKey(entity.getPaymentKey())
                .paymentMethod(entity.getPaymentMethod())
                .paymentLines(lines)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public PaymentLineEntity toLineEntity(PaymentLine line, PaymentEntity paymentEntity) {
        return PaymentLineEntity.builder()
                .payment(paymentEntity)
                .orderLineId(line.getOrderLineId())
                .sellerId(line.getSellerId())
                .amount(line.getAmount())
                .build();
    }

    private PaymentLine toLineDomain(PaymentLineEntity entity) {
        return PaymentLine.reconstructBuilder()
                .id(new PaymentLineId(entity.getId()))
                .orderLineId(entity.getOrderLineId())
                .sellerId(entity.getSellerId())
                .amount(entity.getAmount())
                .build();
    }
}
