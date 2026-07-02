package kr.magicbox.auth.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UnsupportedUserRoleException;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.dto.result.UserResult;
import kr.magicbox.auth.application.port.out.EmailUserPort;
import kr.magicbox.auth.domain.enums.UserRole;
import kr.magicbox.auth.domain.vo.UserId;
import kr.magicbox.auth.grpc.user.GrpcUserRole;
import kr.magicbox.auth.grpc.user.LoginWithEmailRequest;
import kr.magicbox.auth.grpc.user.SignupWithEmailRequest;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEmailGrpcAdapter implements EmailUserPort {

    private final ManagedChannel userManagedChannel;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "signupFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "signupFallback")
    public CompletableFuture<UserResult> signupWithEmail(String email, String passwordHash, String nickname) {
        SignupWithEmailRequest request = SignupWithEmailRequest.newBuilder()
                .setEmail(email)
                .setPasswordHash(passwordHash)
                .setNickname(nickname != null ? nickname : "")
                .build();

        return GrpcFutures.toCompletable(
                UserServiceGrpc.newFutureStub(userManagedChannel).signupWithEmail(request)
        ).thenApply(response -> new UserResult(UserId.of(response.getUserId()), toUserRole(response.getUserRole())));
    }

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "loginFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "loginFallback")
    public CompletableFuture<UserResult> verifyEmailCredential(String email, String rawPassword) {
        LoginWithEmailRequest request = LoginWithEmailRequest.newBuilder()
                .setEmail(email)
                .setRawPassword(rawPassword)
                .build();

        return GrpcFutures.toCompletable(
                UserServiceGrpc.newFutureStub(userManagedChannel).loginWithEmail(request)
        ).thenApply(response -> new UserResult(UserId.of(response.getUserId()), toUserRole(response.getUserRole())));
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
    private CompletableFuture<UserResult> signupFallback(String email, String passwordHash, String nickname, Throwable throwable) {
        log.warn("User 서비스 연결 실패 (signup): {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private CompletableFuture<UserResult> loginFallback(String email, String rawPassword, Throwable throwable) {
        log.warn("User 서비스 연결 실패 (email login): {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
