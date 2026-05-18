package kr.magicbox.payment.adapter.out.toss;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.payment.adapter.out.toss.dto.TossCancelRequest;
import kr.magicbox.payment.adapter.out.toss.dto.TossConfirmRequest;
import kr.magicbox.payment.adapter.out.toss.dto.TossConfirmResponse;
import kr.magicbox.payment.application.dto.result.TossPaymentCancelResult;
import kr.magicbox.payment.application.dto.result.TossPaymentConfirmResult;
import kr.magicbox.payment.application.port.out.TossPaymentPort;
import kr.magicbox.payment.domain.enums.TossPaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TossPaymentClientAdapter implements TossPaymentPort {

    private final TossPaymentFeignClient tossPaymentFeignClient;

    @Override
    @CircuitBreaker(name = "toss-payment")
    public TossPaymentConfirmResult confirm(String paymentKey, Long orderId, long amount) {
        TossConfirmResponse response = tossPaymentFeignClient.confirm(
                TossConfirmRequest.builder()
                        .paymentKey(paymentKey)
                        .orderId(orderId.toString())
                        .amount(amount)
                        .build()
        );
        return TossPaymentConfirmResult.builder()
                .paymentKey(response.paymentKey())
                .paymentMethod(TossPaymentMethod.fromTossMethod(response.method()))
                .orderId(Long.parseLong(response.orderId()))
                .totalAmount(response.totalAmount())
                .build();
    }

    @Override
    @CircuitBreaker(name = "toss-payment")
    public TossPaymentCancelResult cancel(String paymentKey, String cancelReason) {
        tossPaymentFeignClient.cancel(
                paymentKey,
                TossCancelRequest.builder()
                        .cancelReason(cancelReason)
                        .build()
        );
        return TossPaymentCancelResult.builder()
                .paymentKey(paymentKey)
                .cancelledAt(Instant.now())
                .build();
    }
}
