package kr.magicbox.user.adapter.in;

import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.application.dto.GetUserProfileResult;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class UserProfileQueryController {
    private final UserProfileQueryUseCase userProfileQueryUseCase;

    @GetMapping("/{nickname}")
    public ResponseEntity<GetUserProfileResult> getUserProfile(@PathVariable @NotNull String nickname) {
        GetUserProfileResult getUserProfileResult = userProfileQueryUseCase.getUserProfile(nickname);
        return ResponseEntity.ok(getUserProfileResult);
    }
}