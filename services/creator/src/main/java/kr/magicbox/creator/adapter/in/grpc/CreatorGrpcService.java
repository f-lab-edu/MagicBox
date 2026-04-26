package kr.magicbox.creator.adapter.in.grpc;

import io.grpc.stub.StreamObserver;
import kr.magicbox.creator.application.dto.query.IsCreatorOwnedByUserQuery;
import kr.magicbox.creator.application.port.in.IsCreatorOwnedByUserUseCase;
import kr.magicbox.creator.domain.vo.CreatorId;
import kr.magicbox.creator.domain.vo.UserId;
import kr.magicbox.creator.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.creator.grpc.creator.IsCreatorOwnedByUserRequest;
import kr.magicbox.creator.grpc.creator.IsCreatorOwnedByUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CreatorGrpcService extends CreatorServiceGrpc.CreatorServiceImplBase {
    private final IsCreatorOwnedByUserUseCase isCreatorOwnedByUserUseCase;

    @Override
    public void isCreatorOwnedByUser(IsCreatorOwnedByUserRequest request,
                                     StreamObserver<IsCreatorOwnedByUserResponse> responseObserver) {
        boolean ownedByUser = isCreatorOwnedByUserUseCase.isOwnedByUser(
                IsCreatorOwnedByUserQuery.of(
                        CreatorId.of(request.getCreatorId()),
                        UserId.of(request.getUserId())
                )
        );

        responseObserver.onNext(IsCreatorOwnedByUserResponse.newBuilder()
                .setOwnedByUser(ownedByUser)
                .build());
        responseObserver.onCompleted();
    }
}