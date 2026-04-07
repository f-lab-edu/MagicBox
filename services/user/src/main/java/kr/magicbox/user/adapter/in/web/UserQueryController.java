package kr.magicbox.user.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import kr.magicbox.user.adapter.in.web.dto.GetUserProfileResponse;
import kr.magicbox.user.application.port.in.UserQueryUseCase;
import kr.magicbox.user.domain.vo.Nickname;
import kr.magicbox.user.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserQueryController {
    private final UserQueryUseCase userQueryUseCase;

    @GetMapping("/{nickname}")
    public ResponseEntity<GetUserProfileResponse> getUserProfile(
            @PathVariable @NotNull(message = "닉네임은 필수 값입니다.") String nickname,
            @AuthenticationPrincipal UserId requestUserId) {
        var result = userQueryUseCase.getUserProfile(Nickname.of(nickname), requestUserId);
        return ResponseEntity.ok(GetUserProfileResponse.builder()
                .nickname(result.nickname())
                .profile(result.profile())
                .reviews(result.reviews())
                .role(result.role())
                .isMe(result.isMe())
                .build());
    }
}
