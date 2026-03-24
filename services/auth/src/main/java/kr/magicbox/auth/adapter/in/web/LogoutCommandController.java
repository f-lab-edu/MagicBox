package kr.magicbox.auth.adapter.in.web;

import kr.magicbox.auth.application.dto.LogoutCommand;
import kr.magicbox.auth.application.port.in.LogoutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutCommandController {
    private final LogoutUseCase logoutUseCase;
    private final CookieManager cookieManager;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long userId) {
        logoutUseCase.logout(LogoutCommand.builder().userId(userId).build());
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieManager.deleteRefreshTokenCookie().toString())
                .build();
    }
}