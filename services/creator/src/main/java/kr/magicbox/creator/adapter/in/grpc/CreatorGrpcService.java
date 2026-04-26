package kr.magicbox.creator.adapter.in.grpc;

import io.grpc.stub.StreamObserver;
import kr.magicbox.creator.application.dto.query.CheckCreatorAndSubscriberSamePersonQuery;
import kr.magicbox.creator.application.port.in.CheckCreatorAndSubscriberSamePersonUseCase;
import kr.magicbox.creator.domain.vo.CreatorId;
import kr.magicbox.creator.domain.vo.UserId;
import kr.magicbox.creator.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.creator.grpc.creator.IsCreatorAndSubscriberSamePersonRequest;
import kr.magicbox.creator.grpc.creator.IsCreatorAndSubscriberSamePersonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CreatorGrpcService extends CreatorServiceGrpc.CreatorServiceImplBase {
    private final CheckCreatorAndSubscriberSamePersonUseCase checkCreatorAndSubscriberSamePersonUseCase;

    @Override
    public void isCreatorAndSubscriberSamePerson(IsCreatorAndSubscriberSamePersonRequest request,
                                                 StreamObserver<IsCreatorAndSubscriberSamePersonResponse> responseObserver) {
        boolean samePerson = checkCreatorAndSubscriberSamePersonUseCase.isSamePerson(
                CheckCreatorAndSubscriberSamePersonQuery.of(
                        CreatorId.of(request.getCreatorId()),
                        UserId.of(request.getSubscriberId())
                )
        );

        responseObserver.onNext(IsCreatorAndSubscriberSamePersonResponse.newBuilder()
                .setSamePerson(samePerson)
                .build());
        responseObserver.onCompleted();
    }
}
