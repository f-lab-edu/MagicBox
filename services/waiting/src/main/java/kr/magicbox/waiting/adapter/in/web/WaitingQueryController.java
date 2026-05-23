package kr.magicbox.waiting.adapter.in.web;

import kr.magicbox.waiting.adapter.in.web.dto.response.WaitingStatusResponse;
import kr.magicbox.waiting.application.port.in.GetWaitingStatusUseCase;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingQueryController {

    private final GetWaitingStatusUseCase getWaitingStatusUseCase;

    @GetMapping("/{releaseId}/status")
    public Mono<ResponseEntity<WaitingStatusResponse>> getStatus(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long releaseId
    ) {
        return getWaitingStatusUseCase.getStatus(ReleaseId.of(releaseId), userId)
                .map(result -> ResponseEntity.ok(WaitingStatusResponse.from(result)));
    }
}
