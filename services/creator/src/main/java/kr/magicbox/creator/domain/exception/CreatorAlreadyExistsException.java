package kr.magicbox.creator.domain.exception;

import kr.magicbox.creator.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CreatorAlreadyExistsException extends BaseException {

    public CreatorAlreadyExistsException() {
        super("이미 등록된 크리에이터입니다.", HttpStatus.CONFLICT);
    }
}
