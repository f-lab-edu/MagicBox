package kr.magicbox.waiting.application.dto;

import lombok.Builder;

@Builder
public record WaitingStatusResult(
        long rank,
        long queueSize,
        long estimatedWaitSeconds,
        String purchaseToken
) {}
