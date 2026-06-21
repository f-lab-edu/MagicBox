package kr.magicbox.creator.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.SubscribeServiceUnavailableException;
import kr.magicbox.creator.application.port.out.SubscribeQueryPort;
import kr.magicbox.creator.grpc.subscribe.GetSubscriberCountRequest;
import kr.magicbox.creator.grpc.subscribe.GetSubscriberCountResponse;
import kr.magicbox.creator.grpc.subscribe.IsSubscribedRequest;
import kr.magicbox.creator.grpc.subscribe.IsSubscribedResponse;
import kr.magicbox.creator.grpc.subscribe.SubscribeServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscribeGrpcAdapter implements SubscribeQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "subscribeService", fallbackMethod = "getSubscriberCountFallback")
    @TimeLimiter(name = "subscribeService", fallbackMethod = "getSubscriberCountFallback")
    public CompletableFuture<Long> getSubscriberCount(Long creatorId) {
        GetSubscriberCountRequest request = GetSubscriberCountRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.SUBSCRIBE.getHostName());
        SubscribeServiceGrpc.SubscribeServiceFutureStub stub = SubscribeServiceGrpc.newFutureStub(channel);
        ListenableFuture<GetSubscriberCountResponse> future = stub.getSubscriberCount(request);
        try {
            GetSubscriberCountResponse response = future.get();
            return CompletableFuture.completedFuture(response.getSubscriberCount());
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @CircuitBreaker(name = "subscribeService", fallbackMethod = "isSubscribedFallback")
    @TimeLimiter(name = "subscribeService", fallbackMethod = "isSubscribedFallback")
    public CompletableFuture<Boolean> isSubscribed(Long creatorId, Long userId) {
        IsSubscribedRequest request = IsSubscribedRequest.newBuilder()
                .setCreatorId(creatorId)
                .setUserId(userId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.SUBSCRIBE.getHostName());
        SubscribeServiceGrpc.SubscribeServiceFutureStub stub = SubscribeServiceGrpc.newFutureStub(channel);
        ListenableFuture<IsSubscribedResponse> future = stub.isSubscribed(request);
        try {
            IsSubscribedResponse response = future.get();
            return CompletableFuture.completedFuture(response.getSubscribed());
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Long> getSubscriberCountFallback(Long creatorId, Throwable throwable) {
        log.warn("구독 서비스 연결 실패");
        throw new SubscribeServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Boolean> isSubscribedFallback(Long creatorId, Long userId, Throwable throwable) {
        log.warn("구독 서비스 연결 실패");
        throw new SubscribeServiceUnavailableException(throwable);
    }
}
