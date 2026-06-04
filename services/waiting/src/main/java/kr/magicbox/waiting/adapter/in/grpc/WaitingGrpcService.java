package kr.magicbox.waiting.adapter.in.grpc;

import io.grpc.stub.StreamObserver;
import kr.magicbox.waiting.application.port.in.ValidatePurchaseTokenUseCase;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import kr.magicbox.waiting.grpc.waiting.ValidatePurchaseTokenRequest;
import kr.magicbox.waiting.grpc.waiting.ValidatePurchaseTokenResponse;
import kr.magicbox.waiting.grpc.waiting.WaitingServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class WaitingGrpcService extends WaitingServiceGrpc.WaitingServiceImplBase {

    private final ValidatePurchaseTokenUseCase validatePurchaseTokenUseCase;

    @Override
    public void validatePurchaseToken(ValidatePurchaseTokenRequest request,
                                      StreamObserver<ValidatePurchaseTokenResponse> responseObserver) {
        validatePurchaseTokenUseCase.validate(
                        ReleaseId.of(request.getReleaseId()),
                        UserId.of(request.getUserId()),
                        request.getPurchaseToken())
                .subscribe(valid -> {
                    responseObserver.onNext(ValidatePurchaseTokenResponse.newBuilder()
                            .setValid(valid)
                            .build());
                    responseObserver.onCompleted();
                }, responseObserver::onError);
    }
}
