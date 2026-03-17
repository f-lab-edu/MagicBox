package kr.magicbox.user.adapter.in;

import jakarta.validation.Valid;
import kr.magicbox.user.application.dto.UpdateUserProfileCommand;
import kr.magicbox.user.application.port.in.UserProfileCommandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/profile")
@RequiredArgsConstructor
public class UserProfileCommandController {
    private final UserProfileCommandUseCase userProfileCommandUseCase;

    @PatchMapping
    public ResponseEntity<Void> updateUserProfile(@RequestBody @Valid UpdateUserProfileCommand command) {
        userProfileCommandUseCase.updateUserProfile(command);
        return ResponseEntity.noContent().build();
    }
}
