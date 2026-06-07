package kr.magicbox.payment.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.payment.adapter.in.web.dto.request.ApprovePaymentRequest;
import kr.magicbox.payment.adapter.in.web.dto.request.CancelPaymentRequest;
import kr.magicbox.payment.adapter.in.web.dto.response.PaymentResponse;
import kr.magicbox.payment.application.port.in.ApprovePaymentUseCase;
import kr.magicbox.payment.application.port.in.CancelPaymentUseCase;
import kr.magicbox.payment.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentCommandController {

    private final ApprovePaymentUseCase approvePaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<PaymentResponse> approvePayment(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId,
            @Valid @RequestBody ApprovePaymentRequest request
    ) {
        return ResponseEntity.ok(
                PaymentResponse.from(approvePaymentUseCase.approvePayment(request.toCommand(orderId, userId.value())))
        );
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelPayment(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderId,
            @Valid @RequestBody CancelPaymentRequest request
    ) {
        cancelPaymentUseCase.cancelPayment(request.toCommand(orderId, userId.value()));
        return ResponseEntity.noContent().build();
    }
}
