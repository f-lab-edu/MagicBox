package kr.magicbox.user.adapter.in.web;

import jakarta.validation.Valid;
import kr.magicbox.user.adapter.in.web.dto.UpdateUserProfileRequest;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import kr.magicbox.user.application.port.in.WithdrawUserUseCase;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserProfileCommandUseCase userProfileCommandUseCase;
    private final WithdrawUserUseCase withdrawUserUseCase;

    @PatchMapping
    public ResponseEntity<Void> updateUserProfile(@AuthenticationPrincipal UserId userId,
                                                  @RequestBody @Valid UpdateUserProfileRequest request) {
        userProfileCommandUseCase.updateUserProfile(userId, request.toCommand());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserId userId) {
        withdrawUserUseCase.withdrawUser(userId);
        return ResponseEntity.noContent().build();
    }
}