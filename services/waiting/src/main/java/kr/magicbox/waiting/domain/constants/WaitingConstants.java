package kr.magicbox.waiting.domain.constants;

public final class WaitingConstants {

    private WaitingConstants() {}

    /** 1인당 평균 구매 소요 시간 (초) */
    public static final long AVG_PURCHASE_SECONDS = 30;

    /** purchase_token TTL (초) */
    public static final long PURCHASE_TOKEN_TTL_SECONDS = 300;

    /** Polling 응답 권장 재요청 간격 (초) */
    public static final long POLLING_INTERVAL_SECONDS = 10;
}
