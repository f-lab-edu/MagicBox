package kr.magicbox.auth.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UnsupportedUserRoleException;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.application.port.out.UserCredentialPort;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;
import kr.magicbox.auth.grpc.user.GrpcOAuth2Provider;
import kr.magicbox.auth.grpc.user.GrpcUserRole;
import kr.magicbox.auth.grpc.user.LoadUserCredentialRequest;
import kr.magicbox.auth.grpc.user.LoadUserCredentialResponse;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserGrpcAdapter implements UserCredentialPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "loadCredentialFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "loadCredentialFallback")
    public CompletableFuture<UserResult> loadCredential(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage) {
        LoadUserCredentialRequest request = LoadUserCredentialRequest.newBuilder()
                .setOauth2Id(oauth2Id)
                .setProvider(provider)
                .setEmail(email)
                .setProfileImage(profileImage != null ? profileImage : "")
                .build();

        UserServiceGrpc.UserServiceFutureStub stub = UserServiceGrpc.newFutureStub(
                grpcChannelFactory.createChannel(ServiceHost.USER.getHostName()));
        ListenableFuture<LoadUserCredentialResponse> future = stub.loadUserCredential(request);
        LoadUserCredentialResponse response = Futures.getUnchecked(future);

        return CompletableFuture.completedFuture(
                new UserResult(UserId.of(response.getUserId()), toUserRole(response.getUserRole())));
    }

    private UserRole toUserRole(GrpcUserRole grpcUserRole) {
        return switch (grpcUserRole) {
            case USER -> UserRole.USER;
            case CREATOR -> UserRole.CREATOR;
            case ADMIN -> UserRole.ADMIN;
            default -> throw new UnsupportedUserRoleException(grpcUserRole.name());
        };
    }

    @SuppressWarnings("unused")
    private CompletableFuture<UserResult> loadCredentialFallback(String oauth2Id, GrpcOAuth2Provider provider, String email, String profileImage, Throwable throwable) {
        log.warn("User 서비스 연결 실패: {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
