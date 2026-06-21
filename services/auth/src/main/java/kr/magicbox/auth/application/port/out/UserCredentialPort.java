package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.grpc.user.GrpcOAuth2Provider;

import java.util.concurrent.CompletableFuture;

public interface UserCredentialPort {
    CompletableFuture<UserResult> loadCredential(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage);
}
