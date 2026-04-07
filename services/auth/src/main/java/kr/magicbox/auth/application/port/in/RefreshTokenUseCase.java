package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.command.RefreshTokenCommand;
import kr.magicbox.auth.application.dto.result.TokenResult;

public interface RefreshTokenUseCase {
    TokenResult refresh(RefreshTokenCommand command);
}
