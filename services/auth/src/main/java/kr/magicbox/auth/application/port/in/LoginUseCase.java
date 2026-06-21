package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.LoginCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;

import java.util.concurrent.ExecutionException;

public interface LoginUseCase {
    TokenResult login(LoginCommand command) throws ExecutionException, InterruptedException;
}
