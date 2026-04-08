package kr.magicbox.subscribe.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.subscribe.adapter.out.communication.ServiceHost;
import kr.magicbox.subscribe.application.port.out.CreatorIdentityQueryPort;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.global.exception.SystemError;
import kr.magicbox.subscribe.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.subscribe.grpc.creator.IsCreatorAndSubscriberSamePersonRequest;
import kr.magicbox.subscribe.grpc.creator.IsCreatorAndSubscriberSamePersonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatorGrpcAdapter implements CreatorIdentityQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "isCreatorAndSubscriberSamePersonFallback")
    public boolean isCreatorAndSubscriberSamePerson(CreatorId creatorId, SubscriberId subscriberId) {
        IsCreatorAndSubscriberSamePersonRequest request = IsCreatorAndSubscriberSamePersonRequest.newBuilder()
                .setCreatorId(creatorId.value())
                .setSubscriberId(subscriberId.value())
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.CREATOR.getHostName());
        CreatorServiceGrpc.CreatorServiceBlockingStub stub = CreatorServiceGrpc.newBlockingStub(channel);
        IsCreatorAndSubscriberSamePersonResponse response = stub.isCreatorAndSubscriberSamePerson(request);

        return response.getSamePerson();
    }

    private boolean isCreatorAndSubscriberSamePersonFallback(CreatorId creatorId,
                                                             SubscriberId subscriberId,
                                                             Throwable throwable) {
        log.warn("creator 서비스 연결 실패");
        throw new SystemError("creator 서비스 호출을 할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, throwable);
    }
}
