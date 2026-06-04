package kr.magicbox.waiting.global.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "waiting")
public class WaitingProperties {

    /** 1인당 평균 구매 소요 시간 (초) */
    private final long avgPurchaseSeconds;

    /** purchase_token TTL (초) */
    private final long purchaseTokenTtlSeconds;

    /** Polling 응답 권장 재요청 간격 (초) */
    private final long pollingIntervalSeconds;

    /** 대기열 Redis ZSet 키 prefix */
    private final String queueKeyPrefix;

    /** 유저-릴리즈 매핑 Redis 키 prefix */
    private final String userReleaseKeyPrefix;

    /** purchase_token Redis 키 prefix */
    private final String tokenKeyPrefix;

    /** AIMD 혼잡 시 배치 크기 감소 비율 */
    private final double admissionDecreaseRatio;

    /** 배치 크기 최대 비율 (잔여 수량 대비) */
    private final double admissionMaxBatchRatio;

    /** 초기 배치 크기 비율 (잔여 수량 대비, %) */
    private final int admissionInitialBatchRatioPercent;
}
