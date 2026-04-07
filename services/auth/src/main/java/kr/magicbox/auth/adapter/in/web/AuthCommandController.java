package kr.magicbox.auth.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.auth.adapter.in.web.dto.AccessTokenResponse;
import kr.magicbox.auth.adapter.in.web.dto.LoginRequest;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.LoginUseCase;
import kr.magicbox.auth.application.port.in.LogoutUseCase;
import kr.magicbox.auth.application.port.in.RefreshTokenUseCase;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthCommandController {
    private final LoginUseCase loginUseCase;
    private final LogoutUseCase logoutUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final CookieManager cookieManager;

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResult result = loginUseCase.login(request.toCommand());
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken().refreshTokenValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(AccessTokenResponse.builder()
                        .accessToken(result.accessToken().accessTokenValue())
                        .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@CookieValue(name = "refresh_token") String refreshToken) {
        TokenResult result = refreshTokenUseCase.refresh(refreshToken);
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken().refreshTokenValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(AccessTokenResponse.builder()
                        .accessToken(result.accessToken().accessTokenValue())
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserId userId) {
        logoutUseCase.logout(userId);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieManager.deleteRefreshTokenCookie().toString())
                .build();
    }
}
