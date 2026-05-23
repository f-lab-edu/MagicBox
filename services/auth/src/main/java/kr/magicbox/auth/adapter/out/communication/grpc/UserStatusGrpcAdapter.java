package kr.magicbox.auth.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.auth.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.auth.application.port.out.UserStatusPort;
import kr.magicbox.auth.grpc.user.CheckUserActiveRequest;
import kr.magicbox.auth.grpc.user.CheckUserActiveResponse;
import kr.magicbox.auth.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatusGrpcAdapter implements UserStatusPort {

    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "checkUserActiveFallback")
    public boolean isActive(Long userId) {
        CheckUserActiveRequest request = CheckUserActiveRequest.newBuilder()
                .setUserId(userId)
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc
                .newBlockingStub(grpcChannelFactory.createChannel(ServiceHost.USER.getHostName()))
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        CheckUserActiveResponse response = stub.checkUserActive(request);

        return response.getActive();
    }

    @SuppressWarnings("unused")
    private boolean checkUserActiveFallback(Long userId, Throwable throwable) {
        log.warn("User 서비스 연결 실패: {}", throwable.getMessage());
        throw new UserServiceUnavailableException(throwable);
    }
}
