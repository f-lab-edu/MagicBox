package kr.magicbox.auth.application.port.out;

import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.grpc.user.GrpcOAuth2Provider;

public interface UserCredentialPort {
    UserResult loadCredential(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage);
}
