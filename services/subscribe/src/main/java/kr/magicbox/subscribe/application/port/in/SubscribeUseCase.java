package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.command.SubscribeCommand;

import java.util.concurrent.ExecutionException;

public interface SubscribeUseCase {
    void subscribe(SubscribeCommand command) throws ExecutionException, InterruptedException;
}
