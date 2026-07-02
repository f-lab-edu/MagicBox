package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.command.LoginWithEmailCommand;
import kr.magicbox.user.application.dto.result.EmailCredentialResult;

public interface LoginWithEmailUseCase {
    EmailCredentialResult loginWithEmail(LoginWithEmailCommand command);
}
