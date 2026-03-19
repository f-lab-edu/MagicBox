package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.TokenResult;

public interface ReissueTokenUseCase {
    TokenResult reissueToken(String refreshToken);
}