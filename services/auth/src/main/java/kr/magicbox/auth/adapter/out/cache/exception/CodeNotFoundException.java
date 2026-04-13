package kr.magicbox.auth.adapter.out.cache.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class CodeNotFoundException extends BusinessException {
    public CodeNotFoundException() {
        super("해당 코드를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
