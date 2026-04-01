package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.LoginCommand;
import kr.magicbox.auth.application.dto.TokenResult;

public interface LoginUseCase {
    TokenResult login(LoginCommand command);
}
