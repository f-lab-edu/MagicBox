package kr.magicbox.creator.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.creator.adapter.out.communication.ServiceHost;
import kr.magicbox.creator.adapter.out.communication.grpc.exception.ShortformServiceUnavailableException;
import kr.magicbox.creator.application.port.out.ShortformQueryPort;
import kr.magicbox.creator.grpc.shortform.GetShortformsByCreatorIdRequest;
import kr.magicbox.creator.grpc.shortform.GetShortformsByCreatorIdResponse;
import kr.magicbox.creator.grpc.shortform.ShortformServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShortformQueryGrpcAdapter implements ShortformQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "shortformService", fallbackMethod = "getShortformsFallback")
    public List<Object> getShortforms(Long creatorId) {
        GetShortformsByCreatorIdRequest request = GetShortformsByCreatorIdRequest.newBuilder()
                .setCreatorId(creatorId)
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.SHORTFORM.getHostName());
        ShortformServiceGrpc.ShortformServiceBlockingStub stub = ShortformServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        GetShortformsByCreatorIdResponse response = stub.getShortformsByCreatorId(request);

        return response.getShortformsList().stream()
                .map(Object.class::cast)
                .toList();
    }

    @SuppressWarnings("unused")
    private List<Object> getShortformsFallback(Long creatorId, Throwable throwable) {
        log.warn("숏폼 서비스 연결 실패");
        throw new ShortformServiceUnavailableException(throwable);
    }
}
