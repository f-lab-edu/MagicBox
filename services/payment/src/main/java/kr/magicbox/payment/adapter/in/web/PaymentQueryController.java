package kr.magicbox.payment.adapter.in.web;

import kr.magicbox.payment.adapter.in.web.dto.response.PaymentResponse;
import kr.magicbox.payment.application.port.in.GetPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentQueryController {

    private final GetPaymentUseCase getPaymentUseCase;

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(PaymentResponse.from(getPaymentUseCase.getPayment(orderId)));
    }
}
