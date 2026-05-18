package kr.magicbox.creator.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.UserServiceUnavailableException;
import kr.magicbox.creator.application.port.out.UserNicknameQueryPort;
import kr.magicbox.creator.domain.vo.UserId;
import kr.magicbox.creator.grpc.user.GetUserNicknameRequest;
import kr.magicbox.creator.grpc.user.GetUserNicknameResponse;
import kr.magicbox.creator.grpc.user.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserNicknameQueryGrpcAdapter implements UserNicknameQueryPort {
    private final ManagedChannel userManagedChannel;

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "getNicknameFallback")
    public String getNickname(UserId userId) {
        GetUserNicknameRequest request = GetUserNicknameRequest.newBuilder()
                .setUserId(userId.value())
                .build();

        UserServiceGrpc.UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(userManagedChannel);
        GetUserNicknameResponse response = stub.getUserNickname(request);

        return response.getNickname();
    }

    @SuppressWarnings("unused")
    private String getNicknameFallback(UserId userId, Throwable throwable) {
        log.warn("유저 서비스 연결 실패");
        throw new UserServiceUnavailableException(throwable);
    }
}