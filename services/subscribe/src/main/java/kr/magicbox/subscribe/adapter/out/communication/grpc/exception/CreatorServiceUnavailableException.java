package kr.magicbox.subscribe.adapter.out.communication.grpc.exception;

import kr.magicbox.subscribe.global.exception.SystemError;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class CreatorServiceUnavailableException extends SystemError {
    public CreatorServiceUnavailableException(Throwable cause) {
        super("creator 서비스 호출을 할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
