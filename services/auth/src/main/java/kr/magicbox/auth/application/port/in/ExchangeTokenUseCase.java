package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.ExchangeTokenCommand;
import kr.magicbox.auth.application.dto.TokenResult;

public interface ExchangeTokenUseCase {
    TokenResult exchange(ExchangeTokenCommand command);
}