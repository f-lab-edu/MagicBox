package kr.magicbox.user.adapter.in.web;

import kr.magicbox.user.application.port.in.BanUserUseCase;
import kr.magicbox.user.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AdminUserCommandController {

    private final BanUserUseCase banUserUseCase;

    @PatchMapping("/{nickname}/ban")
    public ResponseEntity<Void> banUser(@PathVariable String nickname) {
        banUserUseCase.banUser(Nickname.of(nickname));
        return ResponseEntity.noContent().build();
    }
}