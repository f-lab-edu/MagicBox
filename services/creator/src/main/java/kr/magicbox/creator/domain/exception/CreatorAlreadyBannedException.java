package kr.magicbox.creator.domain.exception;

import kr.magicbox.creator.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CreatorAlreadyBannedException extends BusinessException {

    public CreatorAlreadyBannedException() {
        super("이미 밴 처리된 크리에이터입니다.", HttpStatus.CONFLICT);
    }
}