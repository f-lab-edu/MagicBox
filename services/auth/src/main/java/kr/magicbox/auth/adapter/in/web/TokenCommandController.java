package kr.magicbox.auth.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.auth.adapter.in.web.dto.AccessTokenResponse;
import kr.magicbox.auth.adapter.in.web.dto.ExchangeTokenRequest;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.ExchangeTokenUseCase;
import kr.magicbox.auth.application.port.in.RefreshTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/token")
@RequiredArgsConstructor
public class TokenCommandController {
    private final ExchangeTokenUseCase exchangeTokenUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final CookieManager cookieManager;

    @PostMapping
    public ResponseEntity<AccessTokenResponse> exchangeToken(@RequestBody @Valid ExchangeTokenRequest request) {
        TokenResult result = exchangeTokenUseCase.exchange(request.toCommand());
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken().refreshTokenValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(AccessTokenResponse.from(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
        TokenResult result = refreshTokenUseCase.refresh(refreshToken);
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken().refreshTokenValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(AccessTokenResponse.from(result));
    }
}
