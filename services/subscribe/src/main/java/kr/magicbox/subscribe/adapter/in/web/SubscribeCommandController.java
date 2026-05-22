package kr.magicbox.subscribe.adapter.in.web;

import kr.magicbox.subscribe.application.dto.command.SubscribeCommand;
import kr.magicbox.subscribe.application.dto.command.UnsubscribeCommand;
import kr.magicbox.subscribe.application.port.in.SubscribeUseCase;
import kr.magicbox.subscribe.application.port.in.UnsubscribeUseCase;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscribeCommandController {
    private final SubscribeUseCase subscribeUseCase;
    private final UnsubscribeUseCase unsubscribeUseCase;

    @PostMapping("/{creatorId}")
    public ResponseEntity<Void> subscribe(@AuthenticationPrincipal UserId userId,
                                          @PathVariable Long creatorId) {
        subscribeUseCase.subscribe(SubscribeCommand.of(SubscriberId.of(userId.value()), CreatorId.of(creatorId)));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{creatorId}")
    public ResponseEntity<Void> unsubscribe(@AuthenticationPrincipal UserId userId,
                                            @PathVariable Long creatorId) {
        unsubscribeUseCase.unsubscribe(UnsubscribeCommand.of(SubscriberId.of(userId.value()), CreatorId.of(creatorId)));
        return ResponseEntity.noContent().build();
    }
}
