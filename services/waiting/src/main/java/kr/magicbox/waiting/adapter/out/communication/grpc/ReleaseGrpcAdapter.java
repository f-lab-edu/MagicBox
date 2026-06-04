package kr.magicbox.waiting.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.waiting.adapter.out.communication.grpc.exception.ReleaseServiceUnavailableException;
import kr.magicbox.waiting.application.port.out.ReleaseQueryPort;
import kr.magicbox.waiting.domain.exception.ReleaseNotFoundException;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.grpc.release.GetRemainingQuantityRequest;
import kr.magicbox.waiting.grpc.release.IsReleaseOnSaleRequest;
import kr.magicbox.waiting.grpc.release.IsReleaseOnSaleResponse;
import kr.magicbox.waiting.grpc.release.ReleaseServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReleaseGrpcAdapter implements ReleaseQueryPort {

    private final ManagedChannel releaseManagedChannel;

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "isOnSaleFallback")
    public Mono<Boolean> isOnSale(ReleaseId releaseId) {
        return Mono.fromCallable(() -> {
            IsReleaseOnSaleRequest request = IsReleaseOnSaleRequest.newBuilder()
                    .setReleaseId(releaseId.value())
                    .build();
            ReleaseServiceGrpc.ReleaseServiceBlockingStub stub = ReleaseServiceGrpc.newBlockingStub(releaseManagedChannel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
            IsReleaseOnSaleResponse response = stub.isReleaseOnSale(request);
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
            ReleaseServiceGrpc.ReleaseServiceBlockingStub stub = ReleaseServiceGrpc.newBlockingStub(releaseManagedChannel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
            return stub.getRemainingQuantity(request).getRemainingQuantity();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @SuppressWarnings("unused")
    private Mono<Boolean> isOnSaleFallback(ReleaseId releaseId, Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusException
                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND) {
            return Mono.error(new ReleaseNotFoundException());
        }
        log.warn("릴리즈 서비스 연결 실패");
        return Mono.error(new ReleaseServiceUnavailableException(throwable));
    }

    @SuppressWarnings("unused")
    private Mono<Integer> getRemainingQuantityFallback(ReleaseId releaseId, Throwable throwable) {
        log.warn("릴리즈 서비스 연결 실패 - 잔여 수량 조회 불가");
        return Mono.error(new ReleaseServiceUnavailableException(throwable));
    }
}
