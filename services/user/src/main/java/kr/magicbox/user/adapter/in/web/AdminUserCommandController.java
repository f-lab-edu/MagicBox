package kr.magicbox.user.adapter.in.web;

import kr.magicbox.user.application.dto.command.BanUserCommand;
import kr.magicbox.user.application.dto.command.UnbanUserCommand;
import kr.magicbox.user.application.port.in.BanUserUseCase;
import kr.magicbox.user.application.port.in.UnbanUserUseCase;
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
    private final UnbanUserUseCase unbanUserUseCase;

    @PatchMapping("/{nickname}/ban")
    public ResponseEntity<Void> banUser(@PathVariable String nickname) {
        banUserUseCase.banUser(BanUserCommand.of(Nickname.of(nickname)));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{nickname}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable String nickname) {
        unbanUserUseCase.unbanUser(UnbanUserCommand.of(Nickname.of(nickname)));
        return ResponseEntity.noContent().build();
    }
}
