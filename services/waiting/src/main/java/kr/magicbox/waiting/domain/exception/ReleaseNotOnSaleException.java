package kr.magicbox.waiting.domain.exception;

import kr.magicbox.waiting.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class ReleaseNotOnSaleException extends BusinessException {

    public ReleaseNotOnSaleException() {
        super("현재 판매 중인 릴리즈가 아닙니다.", HttpStatus.CONFLICT);
    }
}
