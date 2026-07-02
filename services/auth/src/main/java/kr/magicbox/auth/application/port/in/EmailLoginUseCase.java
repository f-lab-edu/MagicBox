package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.EmailLoginCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;

public interface EmailLoginUseCase {
    TokenResult emailLogin(EmailLoginCommand command);
}
