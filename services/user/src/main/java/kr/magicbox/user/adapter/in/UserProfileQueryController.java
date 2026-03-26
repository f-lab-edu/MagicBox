package kr.magicbox.user.adapter.in;

import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class UserProfileQueryController {
    private final UserProfileQueryUseCase userProfileQueryUseCase;

    @GetMapping("/{nickname}")
    public ResponseEntity<GetUserProfileResult> getUserProfile(
            @PathVariable @NotNull(message = "닉네임은 필수 값입니다.") String nickname,
            @AuthenticationPrincipal Long requestUserId) {
        GetUserProfileResult getUserProfileResult = userProfileQueryUseCase.getUserProfile(nickname, requestUserId);
        return ResponseEntity.ok(getUserProfileResult);
    }
}