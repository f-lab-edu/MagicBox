package kr.magicbox.waiting.application.dto;

import lombok.Builder;

@Builder
public record EnqueueResult(
        long rank,
        long queueSize,
        long estimatedWaitSeconds,
        long pollingIntervalSeconds
) {}
