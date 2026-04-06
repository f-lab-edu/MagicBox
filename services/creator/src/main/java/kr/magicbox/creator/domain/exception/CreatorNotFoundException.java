package kr.magicbox.creator.domain.exception;

import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CreatorNotFoundException extends BaseException {

    public CreatorNotFoundException() {
        super("크리에이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
