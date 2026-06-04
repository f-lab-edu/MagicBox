package kr.magicbox.waiting.adapter.in.web.dto.response;

import kr.magicbox.waiting.application.dto.WaitingStatusResult;
import lombok.Builder;

@Builder
public record WaitingStatusResponse(
        long rank,
        long queueSize,
        long estimatedWaitSeconds,
        String purchaseToken
) {
    public static WaitingStatusResponse from(WaitingStatusResult result) {
        return WaitingStatusResponse.builder()
                .rank(result.rank())
                .queueSize(result.queueSize())
                .estimatedWaitSeconds(result.estimatedWaitSeconds())
                .purchaseToken(result.purchaseToken())
                .build();
    }
}
