package kr.magicbox.delivery.adapter.out.communication.sweettracker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.client.SweetTrackerFeignClient;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.dto.SweetTrackerTrackingInfoResponse;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.exception.SweetTrackerServiceUnavailableException;
import kr.magicbox.delivery.adapter.out.communication.sweettracker.properties.SweetTrackerProperties;
import kr.magicbox.delivery.application.dto.result.SweetTrackerTrackingInfo;
import kr.magicbox.delivery.application.port.out.SweetTrackerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(SweetTrackerProperties.class)
public class SweetTrackerClientAdapter implements SweetTrackerPort {

    private final SweetTrackerFeignClient sweetTrackerFeignClient;
    private final SweetTrackerProperties sweetTrackerProperties;

    @Override
    @CircuitBreaker(name = "sweetTrackerService", fallbackMethod = "getTrackingInfoFallback")
    public SweetTrackerTrackingInfo getTrackingInfo(String carrierCode, String trackingNumber) {
        SweetTrackerTrackingInfoResponse response = sweetTrackerFeignClient.getTrackingInfo(
                sweetTrackerProperties.getApiKey(),
                carrierCode,
                trackingNumber
        );

        List<SweetTrackerTrackingInfo.TrackingDetail> details = response.trackingDetails() != null
                ? response.trackingDetails().stream()
                        .map(d -> new SweetTrackerTrackingInfo.TrackingDetail(d.timeString(), d.where(), d.kind(), d.level()))
                        .toList()
                : Collections.emptyList();

        return new SweetTrackerTrackingInfo(
                Boolean.TRUE.equals(response.complete()),
                response.level(),
                details
        );
    }

    @SuppressWarnings("unused")
    private SweetTrackerTrackingInfo getTrackingInfoFallback(String carrierCode, String trackingNumber, Throwable throwable) {
        log.warn("[SweetTracker] 서비스 연결 실패: {}", throwable.getMessage());
        throw new SweetTrackerServiceUnavailableException(throwable);
    }
}
