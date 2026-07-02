package kr.magicbox.subscribe.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatorGrpcAdapter implements CreatorIdentityQueryPort {
    private final ManagedChannel creatorManagedChannel;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "isCreatorOwnedByUserFallback")
    @TimeLimiter(name = "creatorService", fallbackMethod = "isCreatorOwnedByUserFallback")
    public CompletableFuture<Boolean> isCreatorOwnedByUser(CreatorId creatorId, SubscriberId subscriberId) throws Exception {
        IsCreatorOwnedByUserRequest request = IsCreatorOwnedByUserRequest.newBuilder()
                .setCreatorId(creatorId.value())
                .setUserId(subscriberId.value())
                .build();

        CreatorServiceGrpc.CreatorServiceFutureStub stub = CreatorServiceGrpc.newFutureStub(creatorManagedChannel);
        ListenableFuture<IsCreatorOwnedByUserResponse> future = stub.isCreatorOwnedByUser(request);
        IsCreatorOwnedByUserResponse response = future.get();

        return CompletableFuture.completedFuture(response.getOwnedByUser());
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Boolean> isCreatorOwnedByUserFallback(CreatorId creatorId,
                                                                     SubscriberId subscriberId,
                                                                     Throwable throwable) {
        log.warn("creator 서비스 연결 실패: creatorId={}, subscriberId={}",
                creatorId.value(),
                subscriberId.value(),
                throwable);
        throw new CreatorServiceUnavailableException(throwable);
    }
}
