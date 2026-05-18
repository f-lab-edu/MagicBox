package kr.magicbox.delivery.application.port.in;

import kr.magicbox.delivery.application.dto.query.GetDeliveryQuery;
import kr.magicbox.delivery.application.dto.result.DeliveryResult;

public interface GetDeliveryUseCase {
    DeliveryResult getDelivery(GetDeliveryQuery query);
}
