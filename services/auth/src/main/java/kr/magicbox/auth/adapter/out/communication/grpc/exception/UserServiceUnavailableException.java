package kr.magicbox.auth.adapter.out.communication.grpc.exception;

import kr.magicbox.auth.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class UserServiceUnavailableException extends SystemError {
    public UserServiceUnavailableException(Throwable cause) {
        super("User 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
