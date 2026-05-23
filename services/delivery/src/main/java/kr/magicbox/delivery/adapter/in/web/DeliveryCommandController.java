package kr.magicbox.delivery.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.delivery.adapter.in.web.dto.request.StartDeliveryRequest;
import kr.magicbox.delivery.adapter.in.web.dto.response.DeliveryResponse;
import kr.magicbox.delivery.application.dto.result.DeliveryResult;
import kr.magicbox.delivery.application.port.in.StartDeliveryUseCase;
import kr.magicbox.delivery.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryCommandController {

    private final StartDeliveryUseCase startDeliveryUseCase;

    @PostMapping("/{orderLineId}/start")
    public ResponseEntity<DeliveryResponse> startDelivery(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long orderLineId,
            @Valid @RequestBody StartDeliveryRequest request
    ) {
        DeliveryResult result = startDeliveryUseCase.startDelivery(request.toCommand(orderLineId, userId.value()));
        return ResponseEntity.ok(DeliveryResponse.from(result));
    }
}
