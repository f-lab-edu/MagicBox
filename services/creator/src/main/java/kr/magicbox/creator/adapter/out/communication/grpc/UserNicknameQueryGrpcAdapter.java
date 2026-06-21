package kr.magicbox.creator.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.creator.application.port.out.UserNicknameQueryPort;
import kr.magicbox.creator.domain.vo.UserId;
import kr.magicbox.creator.grpc.user.GetUserNicknameRequest;
import kr.magicbox.creator.grpc.user.GetUserNicknameResponse;
import kr.magicbox.creator.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserNicknameQueryGrpcAdapter implements UserNicknameQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "getNicknameFallback")
    @TimeLimiter(name = "userService", fallbackMethod = "getNicknameFallback")
    public CompletableFuture<String> getNickname(UserId userId) {
        GetUserNicknameRequest request = GetUserNicknameRequest.newBuilder()
                .setUserId(userId.value())
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.USER.getHostName());
        UserServiceGrpc.UserServiceFutureStub stub = UserServiceGrpc.newFutureStub(channel);
        ListenableFuture<GetUserNicknameResponse> future = stub.getUserNickname(request);
        try {
            GetUserNicknameResponse response = future.get();
            return CompletableFuture.completedFuture(response.getNickname());
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @SuppressWarnings("unused")
    private CompletableFuture<String> getNicknameFallback(UserId userId, Throwable throwable) {
        log.warn("유저 서비스 연결 실패");
        throw new UserServiceUnavailableException(throwable);
    }
}
