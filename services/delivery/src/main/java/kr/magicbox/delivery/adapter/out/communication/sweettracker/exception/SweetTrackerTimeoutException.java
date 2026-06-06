package kr.magicbox.delivery.adapter.out.communication.sweettracker.exception;

import kr.magicbox.delivery.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class SweetTrackerTimeoutException extends SystemError {

    public SweetTrackerTimeoutException() {
        super("스윗트래커 API 요청이 시간 초과되었습니다.", HttpStatus.GATEWAY_TIMEOUT);
    }
}
