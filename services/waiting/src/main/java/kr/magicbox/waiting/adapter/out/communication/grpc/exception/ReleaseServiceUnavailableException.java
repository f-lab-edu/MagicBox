package kr.magicbox.waiting.adapter.out.communication.grpc.exception;

import kr.magicbox.waiting.global.exception.SystemError;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class ReleaseServiceUnavailableException extends SystemError {

    public ReleaseServiceUnavailableException(Throwable cause) {
        super("릴리즈 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
