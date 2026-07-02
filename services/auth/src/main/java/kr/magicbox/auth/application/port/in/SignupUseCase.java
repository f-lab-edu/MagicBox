package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.SignupCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;

public interface SignupUseCase {
    TokenResult signup(SignupCommand command);
}
