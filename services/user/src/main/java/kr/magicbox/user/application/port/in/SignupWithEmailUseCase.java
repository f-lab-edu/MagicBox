package kr.magicbox.user.application.port.in;

import kr.magicbox.user.application.dto.command.SignupWithEmailCommand;
import kr.magicbox.user.application.dto.result.EmailCredentialResult;

public interface SignupWithEmailUseCase {
    EmailCredentialResult signupWithEmail(SignupWithEmailCommand command);
}
