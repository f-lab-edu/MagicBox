package kr.magicbox.delivery.adapter.in.web;

import kr.magicbox.delivery.adapter.in.web.dto.response.DeliveryResponse;
import kr.magicbox.delivery.application.dto.query.GetDeliveryQuery;
import kr.magicbox.delivery.application.port.in.GetDeliveryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryQueryController {

    private final GetDeliveryUseCase getDeliveryUseCase;

    @GetMapping("/{orderLineId}")
    public ResponseEntity<DeliveryResponse> getDelivery(@PathVariable Long orderLineId) {
        return ResponseEntity.ok(DeliveryResponse.from(getDeliveryUseCase.getDelivery(GetDeliveryQuery.builder()
                .orderLineId(orderLineId)
                .build())));
    }
}
