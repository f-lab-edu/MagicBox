package kr.magicbox.auth.adapter.in.security.oauth2.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidOAuth2UserInfoException extends BusinessException {
    public InvalidOAuth2UserInfoException(String field) {
        super(field + " 정보를 가져올 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
