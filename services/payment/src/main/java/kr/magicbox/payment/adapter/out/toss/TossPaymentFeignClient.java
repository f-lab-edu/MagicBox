package kr.magicbox.payment.adapter.out.toss;

import kr.magicbox.payment.adapter.out.toss.dto.TossCancelRequest;
import kr.magicbox.payment.adapter.out.toss.dto.TossCancelResponse;
import kr.magicbox.payment.adapter.out.toss.dto.TossConfirmRequest;
import kr.magicbox.payment.adapter.out.toss.dto.TossConfirmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "toss-payment",
        url = "${toss.payment.base-url}",
        configuration = TossPaymentFeignConfig.class
)
public interface TossPaymentFeignClient {

    @PostMapping("/v1/payments/confirm")
    TossConfirmResponse confirm(@RequestBody TossConfirmRequest request);

    @PostMapping("/v1/payments/{paymentKey}/cancel")
    TossCancelResponse cancel(
            @PathVariable("paymentKey") String paymentKey,
            @RequestBody TossCancelRequest request
    );
}
