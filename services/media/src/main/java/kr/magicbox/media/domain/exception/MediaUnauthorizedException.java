package kr.magicbox.media.domain.exception;

import kr.magicbox.media.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class MediaUnauthorizedException extends BusinessException {

    public MediaUnauthorizedException() {
        super("해당 미디어에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
