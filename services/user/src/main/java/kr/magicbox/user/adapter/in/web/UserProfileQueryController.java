package kr.magicbox.user.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.adapter.in.web.dto.GetUserProfileResponse;
import kr.magicbox.user.application.port.in.UserProfileQueryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileQueryController {
    private final UserProfileQueryUseCase userProfileQueryUseCase;

    @GetMapping("/{nickname}")
    public ResponseEntity<GetUserProfileResponse> getUserProfile(@PathVariable @NotNull String nickname) {
        return ResponseEntity.ok(GetUserProfileResponse.from(userProfileQueryUseCase.getUserProfile(nickname)));
    }
}