package kr.magicbox.auth.adapter.in.web;

import kr.magicbox.auth.adapter.in.web.constants.CookieConstants;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(CookieConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(CookieConstants.REFRESH_TOKEN_MAX_AGE)
                .sameSite(Cookie.SameSite.STRICT.attributeValue())
                .build();
    }
}
