package kr.magicbox.creator.domain.exception;

import kr.magicbox.creator.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class CertificationNotFoundException extends BusinessException {

    public CertificationNotFoundException() {
        super("심사 신청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}