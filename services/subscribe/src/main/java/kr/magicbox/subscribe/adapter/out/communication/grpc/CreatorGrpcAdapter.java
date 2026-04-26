package kr.magicbox.subscribe.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.subscribe.adapter.out.communication.ServiceHost;
import kr.magicbox.subscribe.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.subscribe.application.port.out.CreatorIdentityQueryPort;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.subscribe.grpc.creator.IsCreatorOwnedByUserRequest;
import kr.magicbox.subscribe.grpc.creator.IsCreatorOwnedByUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreatorGrpcAdapter implements CreatorIdentityQueryPort {
    private final CreatorServiceGrpc.CreatorServiceBlockingStub stub;

    public CreatorGrpcAdapter(GrpcChannelFactory grpcChannelFactory) {
        this.stub = CreatorServiceGrpc.newBlockingStub(
                grpcChannelFactory.createChannel(ServiceHost.CREATOR.getHostName()));
    }

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "isCreatorOwnedByUserFallback")
    public boolean isCreatorOwnedByUser(CreatorId creatorId, SubscriberId subscriberId) {
        IsCreatorOwnedByUserRequest request = IsCreatorOwnedByUserRequest.newBuilder()
                .setCreatorId(creatorId.value())
                .setUserId(subscriberId.value())
                .build();

        IsCreatorOwnedByUserResponse response = stub.isCreatorOwnedByUser(request);

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