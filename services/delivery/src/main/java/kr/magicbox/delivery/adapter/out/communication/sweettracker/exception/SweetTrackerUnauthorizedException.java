package kr.magicbox.delivery.adapter.out.communication.sweettracker.exception;

import kr.magicbox.delivery.global.exception.SystemError;
import org.springframework.http.HttpStatus;

public class SweetTrackerUnauthorizedException extends SystemError {

    public SweetTrackerUnauthorizedException() {
        super("스윗트래커 API 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED);
    }
}
