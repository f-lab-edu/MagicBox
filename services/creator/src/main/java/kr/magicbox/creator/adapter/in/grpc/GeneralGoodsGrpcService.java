package kr.magicbox.creator.adapter.in.grpc;

import io.grpc.stub.StreamObserver;
import kr.magicbox.creator.application.port.in.GetCreatorIdByUserIdUseCase;
import kr.magicbox.creator.domain.vo.UserId;
import kr.magicbox.creator.grpc.generalgoods.GeneralGoodsServiceGrpc;
import kr.magicbox.creator.grpc.generalgoods.GetCreatorIdByUserIdRequest;
import kr.magicbox.creator.grpc.generalgoods.GetCreatorIdByUserIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GeneralGoodsGrpcService extends GeneralGoodsServiceGrpc.GeneralGoodsServiceImplBase {
    private final GetCreatorIdByUserIdUseCase getCreatorIdByUserIdUseCase;

    @Override
    public void getCreatorIdByUserId(GetCreatorIdByUserIdRequest request,
                                     StreamObserver<GetCreatorIdByUserIdResponse> responseObserver) {
        Long creatorId = getCreatorIdByUserIdUseCase.getCreatorIdByUserId(UserId.of(request.getUserId()))
                .value();

        responseObserver.onNext(GetCreatorIdByUserIdResponse.newBuilder()
                .setCreatorId(creatorId)
                .build());
        responseObserver.onCompleted();
    }
}