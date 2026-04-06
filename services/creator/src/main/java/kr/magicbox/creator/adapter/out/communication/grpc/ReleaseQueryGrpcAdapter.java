package kr.magicbox.creator.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.ReleaseServiceUnavailableException;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class ReleaseQueryGrpcAdapter implements ReleaseQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "getReleaseCountFallback")
    public long getReleaseCount(Long creatorId) {
        GetReleaseCountRequest request = GetReleaseCountRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName());
        ReleaseServiceGrpc.ReleaseServiceBlockingStub stub = ReleaseServiceGrpc.newBlockingStub(channel);
        GetReleaseCountResponse response = stub.getReleaseCount(request);

        return response.getReleaseCount();
    }

    @Override
    @CircuitBreaker(name = "releaseService", fallbackMethod = "getReleasesFallback")
    public List<Object> getReleases(Long creatorId) {
        GetReleasesByCreatorIdRequest request = GetReleasesByCreatorIdRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.RELEASE.getHostName());
        ReleaseServiceGrpc.ReleaseServiceBlockingStub stub = ReleaseServiceGrpc.newBlockingStub(channel);
        GetReleasesByCreatorIdResponse response = stub.getReleasesByCreatorId(request);

        return response.getReleasesList().stream()
                .map(release -> (Object) release)
                .toList();
    }

    @SuppressWarnings("unused")
    private long getReleaseCountFallback(Long creatorId, Throwable throwable) {
        log.warn("릴리즈 서비스 연결 실패");
        throw new ReleaseServiceUnavailableException(throwable);
    }

    @SuppressWarnings("unused")
    private List<Object> getReleasesFallback(Long creatorId, Throwable throwable) {
        log.warn("릴리즈 서비스 연결 실패");
        throw new ReleaseServiceUnavailableException(throwable);
    }
}