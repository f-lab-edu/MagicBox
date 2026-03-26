package kr.magicbox.auth.adapter.in.security.oauth2.exception;

import kr.magicbox.auth.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UnsupportedOAuth2ProviderException extends BusinessException {
    public UnsupportedOAuth2ProviderException(String provider) {
        super(provider + " OAuth2는 지원하지 않습니다.", HttpStatus.BAD_REQUEST);
    }
}
