package kr.magicbox.user.adapter.in.grpc;

import kr.magicbox.user.adapter.in.grpc.exception.UnsupportedOAuth2ProviderException;
import kr.magicbox.user.application.dto.LoadUserCredentialCommand;
import kr.magicbox.user.application.dto.LoadUserCredentialResult;
import kr.magicbox.user.application.port.in.LoadUserCredentialUseCase;
import kr.magicbox.user.domain.enums.OAuth2Provider;
import kr.magicbox.user.grpc.user.GrpcOAuth2Provider;
import kr.magicbox.user.grpc.user.LoadUserCredentialRequest;
import kr.magicbox.user.grpc.user.LoadUserCredentialResponse;
import kr.magicbox.user.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcController extends UserServiceGrpc.UserServiceImplBase {

    private final LoadUserCredentialUseCase loadUserCredentialUseCase;

    @Override
    public void loadUserCredential(LoadUserCredentialRequest request,
                                   StreamObserver<LoadUserCredentialResponse> responseObserver) {
        LoadUserCredentialCommand command = LoadUserCredentialCommand.of(
                request.getOauth2Id(),
                toOAuth2Provider(request.getProvider()),
                request.getEmail(),
                request.getProfileImage()
        );

        LoadUserCredentialResult result = loadUserCredentialUseCase.loadUserCredential(command);

        responseObserver.onNext(LoadUserCredentialResponse.newBuilder()
                .setUserId(result.userId())
                .setUserRole(result.userRole())
                .build());
        responseObserver.onCompleted();
    }

    private OAuth2Provider toOAuth2Provider(GrpcOAuth2Provider grpcProvider) {
        return switch (grpcProvider) {
            case GOOGLE -> OAuth2Provider.GOOGLE;
            case NAVER -> OAuth2Provider.NAVER;
            case KAKAO -> OAuth2Provider.KAKAO;
            default -> throw new UnsupportedOAuth2ProviderException(grpcProvider.name());
        };
    }
}
