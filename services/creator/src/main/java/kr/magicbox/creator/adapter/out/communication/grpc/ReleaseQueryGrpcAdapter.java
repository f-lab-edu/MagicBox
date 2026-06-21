package kr.magicbox.creator.adapter.out.communication.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.ReleaseServiceUnavailableException;
import kr.magicbox.creator.application.dto.result.ReleaseId;
import kr.magicbox.creator.application.dto.result.ReleaseLevel;
import kr.magicbox.creator.application.dto.result.ReleaseResult;
import kr.magicbox.creator.application.port.out.ReleaseQueryPort;
import kr.magicbox.creator.grpc.release.GetReleaseCountRequest;
import kr.magicbox.creator.grpc.release.GetReleaseCountResponse;
import kr.magicbox.creator.grpc.release.GetReleasesByCreatorIdRequest;
import kr.magicbox.creator.grpc.release.GetReleasesByCreatorIdResponse;
import kr.magicbox.creator.grpc.release.ReleaseServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReleaseQueryGrpcAdapter implements ReleaseQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "getReleaseCountFallback")
    @TimeLimiter(name = "releaseService", fallbackMethod = "getReleaseCountFallback")
    public CompletableFuture<Long> getReleaseCount(Long creatorId) {
        GetReleaseCountRequest request = GetReleaseCountRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName());
        ReleaseServiceGrpc.ReleaseServiceFutureStub stub = ReleaseServiceGrpc.newFutureStub(channel);
        ListenableFuture<GetReleaseCountResponse> future = stub.getReleaseCount(request);
        try {
            GetReleaseCountResponse response = future.get();
            return CompletableFuture.completedFuture(response.getReleaseCount());
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "getReleasesFallback")
    @TimeLimiter(name = "releaseService", fallbackMethod = "getReleasesFallback")
    public CompletableFuture<List<ReleaseResult>> getReleases(Long creatorId) {
        GetReleasesByCreatorIdRequest request = GetReleasesByCreatorIdRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName());
        ReleaseServiceGrpc.ReleaseServiceFutureStub stub = ReleaseServiceGrpc.newFutureStub(channel);
        ListenableFuture<GetReleasesByCreatorIdResponse> future = stub.getReleasesByCreatorId(request);
        try {
            GetReleasesByCreatorIdResponse response = future.get();
            List<ReleaseResult> releases = response.getReleasesList().stream()
                    .map(release -> ReleaseResult.builder()
                            .releaseId(ReleaseId.of(release.getReleaseId()))
                            .title(release.getTitle())
                            .thumbnailUrl(release.getThumbnailUrl())
                            .level(ReleaseLevel.valueOf(release.getLevel().name()))
                            .creatorNickname(release.getCreatorNickname())
                            .price(release.getPrice())
                            .limitedQuantity(release.getLimitedQuantity())
                            .build())
                    .toList();
            return CompletableFuture.completedFuture(releases);
        } catch (ExecutionException e) {
            return CompletableFuture.failedFuture(e.getCause());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Long> getReleaseCountFallback(Long creatorId, Throwable throwable) {
        log.warn("릴리즈 개수 조회 서비스 연결 실패");
        throw new ReleaseServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private CompletableFuture<List<ReleaseResult>> getReleasesFallback(Long creatorId, Throwable throwable) {
        log.warn("릴리즈 목록 조회 서비스 연결 실패");
        throw new ReleaseServiceUnavailableException(throwable);
    }
}
