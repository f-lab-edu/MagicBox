package kr.magicbox.media.domain.exception;

import kr.magicbox.media.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class MediaNotFoundException extends BusinessException {

    public MediaNotFoundException() {
        super("미디어를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
