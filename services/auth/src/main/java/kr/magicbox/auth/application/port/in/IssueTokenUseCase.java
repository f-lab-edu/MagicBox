package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.application.dto.IssueTokenCommand;
import kr.magicbox.auth.application.dto.IssueTokenResult;

public interface IssueTokenUseCase {
    IssueTokenResult issueToken(IssueTokenCommand command);
}