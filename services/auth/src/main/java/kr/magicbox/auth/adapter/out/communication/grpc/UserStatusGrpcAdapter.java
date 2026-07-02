package kr.magicbox.auth.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.port.out.UserStatusPort;
import kr.magicbox.auth.grpc.user.CheckUserActiveRequest;
import kr.magicbox.auth.grpc.user.CheckUserActiveResponse;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatusGrpcAdapter implements UserStatusPort {

    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "checkUserActiveFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "checkUserActiveFallback")
    public CompletableFuture<Boolean> isActive(Long userId) {
        CheckUserActiveRequest request = CheckUserActiveRequest.newBuilder()
                .setUserId(userId)
                .build();

        UserServiceGrpc.UserServiceFutureStub stub = UserServiceGrpc.newFutureStub(
                grpcChannelFactory.createChannel(ServiceHost.USER.getHostName()));
        ListenableFuture<CheckUserActiveResponse> future = stub.checkUserActive(request);

        CompletableFuture<Boolean> result = new CompletableFuture<>();
        future.addListener(() -> {
            try {
                result.complete(future.get().getActive());
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
        }, Runnable::run);
        return result;
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Boolean> checkUserActiveFallback(Long userId, Throwable throwable) {
        log.warn("User 서비스 연결 실패: {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
