package kr.magicbox.generalgoods.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kr.magicbox.generalgoods.adapter.out.communication.ServiceHost;
import kr.magicbox.generalgoods.adapter.out.communication.grpc.exception.CreatorNotFoundException;
import kr.magicbox.generalgoods.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.generalgoods.application.port.out.CreatorIdQueryPort;
import kr.magicbox.generalgoods.domain.vo.CreatorId;
import kr.magicbox.generalgoods.domain.vo.UserId;
import kr.magicbox.generalgoods.grpc.creator.GeneralGoodsServiceGrpc;
import kr.magicbox.generalgoods.grpc.creator.GetCreatorIdByUserIdRequest;
import kr.magicbox.generalgoods.grpc.creator.GetCreatorIdByUserIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatorGrpcAdapter implements CreatorIdQueryPort {
    private final GrpcChannelFactory grpcChannelFactory;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "getCreatorIdFallback")
    public CreatorId getCreatorId(UserId userId) {
        GetCreatorIdByUserIdRequest request = GetCreatorIdByUserIdRequest.newBuilder()
                .setUserId(userId.value())
                .build();

        ManagedChannel channel = grpcChannelFactory.createChannel(ServiceHost.CREATOR.getHostName());
        GeneralGoodsServiceGrpc.GeneralGoodsServiceBlockingStub stub = GeneralGoodsServiceGrpc.newBlockingStub(channel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        GetCreatorIdByUserIdResponse response = stub.getCreatorIdByUserId(request);

        return new CreatorId(response.getCreatorId());
    }

    @SuppressWarnings("unused")
    private CreatorId getCreatorIdFallback(UserId userId, Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusException
                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND) {
            throw new CreatorNotFoundException();
        }
        log.warn("크리에이터 서비스 연결 실패");
        throw new CreatorServiceUnavailableException(throwable);
    }
}