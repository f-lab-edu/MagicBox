package kr.magicbox.auth.adapter.out.cache.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class RefreshTokenNotFoundException extends BusinessException {
    public RefreshTokenNotFoundException() {
        super("리프레시 토큰을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
