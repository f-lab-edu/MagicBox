package kr.magicbox.user.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.user.adapter.in.web.dto.UpdateUserProfileRequest;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileCommandController {
    private final UserProfileCommandUseCase userProfileCommandUseCase;

    @PatchMapping
    public ResponseEntity<Void> updateUserProfile(@AuthenticationPrincipal Long userId,
                                                   @RequestBody @Valid UpdateUserProfileRequest request) {
        userProfileCommandUseCase.updateUserProfile(userId, request.toCommand());
        return ResponseEntity.noContent().build();
    }
}