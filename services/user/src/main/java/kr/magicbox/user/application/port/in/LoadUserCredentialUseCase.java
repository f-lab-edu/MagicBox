package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.LoadUserCredentialCommand;
import kr.magicbox.user.application.dto.LoadUserCredentialResult;

public interface LoadUserCredentialUseCase {
    LoadUserCredentialResult loadUserCredential(LoadUserCredentialCommand command);
}
