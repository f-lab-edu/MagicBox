package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.command.UnsubscribeCommand;

public interface UnsubscribeUseCase {
    void unsubscribe(UnsubscribeCommand command);
}
