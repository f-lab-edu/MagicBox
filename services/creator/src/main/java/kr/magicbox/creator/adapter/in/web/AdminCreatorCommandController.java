package kr.magicbox.creator.adapter.in.web;

import kr.magicbox.creator.application.dto.command.BanCreatorCommand;
import kr.magicbox.creator.application.dto.command.UnbanCreatorCommand;
import kr.magicbox.creator.application.port.in.BanCreatorUseCase;
import kr.magicbox.creator.application.port.in.UnbanCreatorUseCase;
import kr.magicbox.creator.domain.vo.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@RequiredArgsConstructor
public class AdminCreatorCommandController {
    private final BanCreatorUseCase banCreatorUseCase;
    private final UnbanCreatorUseCase unbanCreatorUseCase;

    @PatchMapping("/{nickname}/ban")
    public ResponseEntity<Void> banCreator(@PathVariable String nickname) {
        banCreatorUseCase.banCreator(BanCreatorCommand.of(Nickname.of(nickname)));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{nickname}/unban")
    public ResponseEntity<Void> unbanCreator(@PathVariable String nickname) {
        unbanCreatorUseCase.unbanCreator(UnbanCreatorCommand.of(Nickname.of(nickname)));
        return ResponseEntity.noContent().build();
    }
}
