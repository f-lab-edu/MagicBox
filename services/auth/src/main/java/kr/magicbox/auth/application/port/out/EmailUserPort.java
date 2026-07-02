package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.application.dto.result.UserResult;

import java.util.concurrent.CompletableFuture;

public interface EmailUserPort {
    CompletableFuture<UserResult> signupWithEmail(String email, String passwordHash, String nickname);
    CompletableFuture<UserResult> verifyEmailCredential(String email, String rawPassword);
}
