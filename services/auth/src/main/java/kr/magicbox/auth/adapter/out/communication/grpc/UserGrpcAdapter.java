package kr.magicbox.auth.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.dto.UserResult;
import kr.magicbox.auth.application.port.out.UserCredentialPort;
import kr.magicbox.auth.grpc.user.LoadUserCredentialRequest;
import kr.magicbox.auth.grpc.user.LoadUserCredentialResponse;
import kr.magicbox.auth.grpc.user.GrpcOAuth2Provider;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGrpcAdapter implements UserCredentialPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "loadCredentialFallback")
    public UserResult loadCredential(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage) {
        LoadUserCredentialRequest request = LoadUserCredentialRequest.newBuilder()
                .setOauth2Id(oauth2Id)
                .setProvider(provider)
                .setEmail(email)
                .setProfileImage(profileImage != null ? profileImage : "")
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(
                grpcChannelFactory.createChannel(ServiceHost.USER.getHostName()));
        LoadUserCredentialResponse response = stub.loadUserCredential(request);

        return new UserResult(response.getUserId(), response.getUserRole());
    }

    @SuppressWarnings("unused")
    private UserResult loadCredentialFallback(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage, Throwable throwable) {
        log.warn("User 서비스 연결 실패: {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
