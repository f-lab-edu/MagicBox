package kr.magicbox.user.adapter.out.communication.grpc.exception;

import kr.magicbox.user.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class GrpcServiceConfigurationNotFoundException extends SystemError {

    public GrpcServiceConfigurationNotFoundException(String serviceName) {
        super("gRPC 서비스 설정을 찾을 수 없습니다: " + serviceName, HttpStatus.SERVICE_UNAVAILABLE);
    }
}