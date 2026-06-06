package kr.magicbox.delivery.application.port.in;

import kr.magicbox.delivery.application.dto.command.StartDeliveryCommand;
import kr.magicbox.delivery.application.dto.result.DeliveryResult;

public interface StartDeliveryUseCase {
    DeliveryResult startDelivery(StartDeliveryCommand command);
}
