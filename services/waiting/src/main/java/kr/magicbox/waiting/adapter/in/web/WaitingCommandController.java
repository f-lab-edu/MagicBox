package kr.magicbox.waiting.adapter.in.web;

import kr.magicbox.waiting.adapter.in.web.dto.response.EnqueueResponse;
import kr.magicbox.waiting.application.port.in.EnqueueUseCase;
import kr.magicbox.waiting.domain.vo.ReleaseId;
import kr.magicbox.waiting.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingCommandController {

    private final EnqueueUseCase enqueueUseCase;

    /**
     * 대기열 입장.
     * 응답의 waitStrategy(SSE|POLLING)에 따라 클라이언트가 연결 방식을 결정한다.
     */
    @PostMapping("/{releaseId}")
    public Mono<ResponseEntity<EnqueueResponse>> enqueue(
            @AuthenticationPrincipal UserId userId,
            @PathVariable Long releaseId
    ) {
        return enqueueUseCase.enqueue(ReleaseId.of(releaseId), userId)
                .map(result -> ResponseEntity.status(HttpStatus.CREATED).body(EnqueueResponse.from(result)));
    }
}
