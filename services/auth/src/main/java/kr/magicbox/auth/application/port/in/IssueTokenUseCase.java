package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.IssueTokenCommand;
import kr.magicbox.auth.application.dto.TokenResult;

public interface IssueTokenUseCase {
    TokenResult issueToken(IssueTokenCommand command);
}