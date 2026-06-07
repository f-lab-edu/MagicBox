package kr.magicbox.delivery.adapter.out.communication.sweettracker.exception;

import kr.magicbox.delivery.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class SweetTrackerServiceUnavailableException extends SystemError {

    public SweetTrackerServiceUnavailableException(Throwable cause) {
        super("스윗트래커 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
