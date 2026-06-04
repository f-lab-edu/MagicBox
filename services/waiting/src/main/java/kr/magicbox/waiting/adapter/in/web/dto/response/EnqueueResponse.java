package kr.magicbox.waiting.adapter.in.web.dto.response;

import kr.magicbox.waiting.application.dto.EnqueueResult;
import lombok.Builder;

@Builder
public record EnqueueResponse(
        long rank,
        long queueSize,
        long estimatedWaitSeconds,
        long pollingIntervalSeconds
) {
    public static EnqueueResponse from(EnqueueResult result) {
        return EnqueueResponse.builder()
                .rank(result.rank())
                .queueSize(result.queueSize())
                .estimatedWaitSeconds(result.estimatedWaitSeconds())
                .pollingIntervalSeconds(result.pollingIntervalSeconds())
                .build();
    }
}
