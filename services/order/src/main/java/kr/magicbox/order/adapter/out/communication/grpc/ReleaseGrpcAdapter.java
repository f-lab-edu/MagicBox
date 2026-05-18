package kr.magicbox.order.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.order.adapter.out.communication.grpc.exception.ReleaseServiceUnavailableException;
import kr.magicbox.order.application.port.out.ReleaseIncreaseSoldQuantityPort;
import kr.magicbox.order.grpc.release.IncreaseSoldQuantityRequest;
import kr.magicbox.order.grpc.release.ReleaseServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReleaseGrpcAdapter implements ReleaseIncreaseSoldQuantityPort {

    private final ManagedChannel releaseManagedChannel;

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "increaseSoldQuantityFallback")
    public void increaseSoldQuantity(Long releaseId) {
        IncreaseSoldQuantityRequest request = IncreaseSoldQuantityRequest.newBuilder()
                .setReleaseId(releaseId)
                .build();

        ReleaseServiceGrpc.ReleaseServiceBlockingStub stub = ReleaseServiceGrpc.newBlockingStub(releaseManagedChannel);
        stub.increaseSoldQuantity(request);
    }

    @SuppressWarnings("unused")
    private void increaseSoldQuantityFallback(Long releaseId, Throwable throwable) {
        log.warn("릴리즈 서비스 연결 실패");
        throw new ReleaseServiceUnavailableException(throwable);
    }
}
