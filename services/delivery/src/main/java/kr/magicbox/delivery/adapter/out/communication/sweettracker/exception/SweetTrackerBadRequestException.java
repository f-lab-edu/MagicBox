package kr.magicbox.delivery.adapter.out.communication.sweettracker.exception;

import kr.magicbox.delivery.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class SweetTrackerBadRequestException extends SystemError {

    public SweetTrackerBadRequestException() {
        super("스윗트래커 API 잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
    }
}
