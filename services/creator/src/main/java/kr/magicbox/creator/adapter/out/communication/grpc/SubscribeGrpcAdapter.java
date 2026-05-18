package kr.magicbox.creator.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.SubscribeServiceUnavailableException;
import kr.magicbox.creator.application.port.out.SubscribeQueryPort;
import kr.magicbox.creator.grpc.subscribe.GetSubscriberCountRequest;
import kr.magicbox.creator.grpc.subscribe.GetSubscriberCountResponse;
import kr.magicbox.creator.grpc.subscribe.IsSubscribedRequest;
import kr.magicbox.creator.grpc.subscribe.IsSubscribedResponse;
import kr.magicbox.creator.grpc.subscribe.SubscribeServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeGrpcAdapter implements SubscribeQueryPort {
    private final ManagedChannel subscribeManagedChannel;

    @Override
    @CircuitBreaker(name = "subscribeService", fallbackMethod = "getSubscriberCountFallback")
    public long getSubscriberCount(Long creatorId) {
        GetSubscriberCountRequest request = GetSubscriberCountRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        SubscribeServiceGrpc.SubscribeServiceBlockingStub stub = SubscribeServiceGrpc.newBlockingStub(subscribeManagedChannel);
        GetSubscriberCountResponse response = stub.getSubscriberCount(request);

        return response.getSubscriberCount();
    }

    @Override
    @CircuitBreaker(name = "subscribeService", fallbackMethod = "isSubscribedFallback")
    public boolean isSubscribed(Long creatorId, Long userId) {
        IsSubscribedRequest request = IsSubscribedRequest.newBuilder()
                .setCreatorId(creatorId)
                .setUserId(userId)
                .build();

        SubscribeServiceGrpc.SubscribeServiceBlockingStub stub = SubscribeServiceGrpc.newBlockingStub(subscribeManagedChannel);
        IsSubscribedResponse response = stub.isSubscribed(request);

        return response.getSubscribed();
    }

    @SuppressWarnings("unused")
    private long getSubscriberCountFallback(Long creatorId, Throwable throwable) {
        log.warn("구독 서비스 연결 실패");
        throw new SubscribeServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private boolean isSubscribedFallback(Long creatorId, Long userId, Throwable throwable) {
        log.warn("구독 서비스 연결 실패");
        throw new SubscribeServiceUnavailableException(throwable);
    }
}