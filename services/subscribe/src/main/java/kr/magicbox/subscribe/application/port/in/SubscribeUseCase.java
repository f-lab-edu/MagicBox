package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.command.SubscribeCommand;

public interface SubscribeUseCase {
    void subscribe(SubscribeCommand command);
}
