package kr.magicbox.waiting.adapter.out.communication.grpc;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.waiting.adapter.out.communication.ServiceHost;
import kr.magicbox.waiting.adapter.out.communication.grpc.exception.ReleaseServiceUnavailableException;
import kr.magicbox.waiting.application.port.out.ReleaseQueryPort;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.grpc.release.GetRemainingQuantityRequest;
import kr.magicbox.waiting.grpc.release.GetRemainingQuantityResponse;
import kr.magicbox.waiting.grpc.release.IsReleaseOnSaleRequest;
import kr.magicbox.waiting.grpc.release.IsReleaseOnSaleResponse;
import kr.magicbox.waiting.grpc.release.ReleaseServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReleaseGrpcAdapter implements ReleaseQueryPort {

    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "isOnSaleFallback")
    public Mono<Boolean> isOnSale(ReleaseId releaseId) {
        return Mono.fromCallable(() -> {
            IsReleaseOnSaleRequest request = IsReleaseOnSaleRequest.newBuilder()
                    .setReleaseId(releaseId.value())
                    .build();

            ReleaseServiceGrpc.ReleaseServiceFutureStub stub =
                    ReleaseServiceGrpc.newFutureStub(grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName()));
            ListenableFuture<IsReleaseOnSaleResponse> future = stub.isReleaseOnSale(request);
            IsReleaseOnSaleResponse response = Futures.getUnchecked(future);

            return response.getOnSale();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "getRemainingQuantityFallback")
    public Mono<Integer> getRemainingQuantity(ReleaseId releaseId) {
        return Mono.fromCallable(() -> {
            GetRemainingQuantityRequest request = GetRemainingQuantityRequest.newBuilder()
                    .setReleaseId(releaseId.value())
                    .build();

            ReleaseServiceGrpc.ReleaseServiceFutureStub stub =
                    ReleaseServiceGrpc.newFutureStub(grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName()));
            ListenableFuture<GetRemainingQuantityResponse> future = stub.getRemainingQuantity(request);
            GetRemainingQuantityResponse response = Futures.getUnchecked(future);

            return response.getRemainingQuantity();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @SuppressWarnings("unused")
    private Mono<Boolean> isOnSaleFallback(ReleaseId releaseId, Throwable throwable) {
        throw new ReleaseServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private Mono<Integer> getRemainingQuantityFallback(ReleaseId releaseId, Throwable throwable) {
        throw new ReleaseServiceUnavailableException(throwable);
    }
}
