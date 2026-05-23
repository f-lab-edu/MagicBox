package kr.magicbox.payment.application.port.out;

import kr.magicbox.payment.domain.event.PaymentDomainEvent;

public interface PaymentOutboxPort {
    void save(PaymentDomainEvent event);
}
