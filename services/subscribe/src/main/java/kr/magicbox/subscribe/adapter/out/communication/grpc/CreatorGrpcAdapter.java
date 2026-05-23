package kr.magicbox.subscribe.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.subscribe.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.subscribe.application.port.out.CreatorIdentityQueryPort;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.subscribe.grpc.creator.IsCreatorOwnedByUserRequest;
import kr.magicbox.subscribe.grpc.creator.IsCreatorOwnedByUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatorGrpcAdapter implements CreatorIdentityQueryPort {
    private final ManagedChannel creatorManagedChannel;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "isCreatorOwnedByUserFallback")
    public boolean isCreatorOwnedByUser(CreatorId creatorId, SubscriberId subscriberId) {
        IsCreatorOwnedByUserRequest request = IsCreatorOwnedByUserRequest.newBuilder()
                .setCreatorId(creatorId.value())
                .setUserId(subscriberId.value())
                .build();

        CreatorServiceGrpc.CreatorServiceBlockingStub stub = CreatorServiceGrpc.newBlockingStub(creatorManagedChannel);
        IsCreatorOwnedByUserResponse response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                .isCreatorOwnedByUser(request);

        return response.getOwnedByUser();
    }

    @SuppressWarnings("unused")
    private boolean isCreatorOwnedByUserFallback(CreatorId creatorId,
                                                 SubscriberId subscriberId,
                                                 Throwable throwable) {
        log.warn("creator 서비스 연결 실패: creatorId={}, subscriberId={}",
                creatorId.value(),
                subscriberId.value(),
                throwable);
        throw new CreatorServiceUnavailableException(throwable);
    }
}
