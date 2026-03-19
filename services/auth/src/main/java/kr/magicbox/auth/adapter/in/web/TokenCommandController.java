package kr.magicbox.auth.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.auth.application.dto.IssueTokenCommand;
import kr.magicbox.auth.application.dto.TokenResult;
import kr.magicbox.auth.application.port.in.IssueTokenUseCase;
import kr.magicbox.auth.application.port.in.ReissueTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/token")
@RequiredArgsConstructor
public class TokenCommandController {
    private final IssueTokenUseCase issueTokenUseCase;
    private final ReissueTokenUseCase reissueTokenUseCase;
    private final CookieManager cookieManager;

    @PostMapping("/issue")
    public ResponseEntity<String> issueToken(
        @RequestBody @Valid IssueTokenCommand command) {
        TokenResult result = issueTokenUseCase.issueToken(command);
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.accessToken());
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissueToken(
        @CookieValue(name = "refresh_token") String refreshToken) {
        TokenResult result = reissueTokenUseCase.reissueToken(refreshToken);
        ResponseCookie cookie = cookieManager.createRefreshTokenCookie(result.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.accessToken());
    }
}