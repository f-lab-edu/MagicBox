package kr.magicbox.delivery.application.service;

import kr.magicbox.delivery.adapter.out.communication.sweettracker.properties.SweetTrackerProperties;
import kr.magicbox.delivery.application.dto.result.SweetTrackerTrackingInfo;
import kr.magicbox.delivery.application.port.out.DeliveryOutboxPort;
import kr.magicbox.delivery.application.port.out.DeliveryRepositoryPort;
import kr.magicbox.delivery.application.port.out.SweetTrackerPort;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import kr.magicbox.delivery.domain.aggregate.TrackingHistory;
import kr.magicbox.delivery.domain.enums.DeliveryStatus;
import kr.magicbox.delivery.domain.enums.TrackingHistoryStatus;
import kr.magicbox.delivery.domain.event.DeliveryCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncDeliveryTrackingService {

    private static final DateTimeFormatter SWEET_TRACKER_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DeliveryRepositoryPort deliveryRepositoryPort;
    private final DeliveryOutboxPort deliveryOutboxPort;
    private final SweetTrackerPort sweetTrackerPort;
    private final SweetTrackerProperties sweetTrackerProperties;

    @Transactional
    public void syncTracking(Delivery delivery) {
        SweetTrackerTrackingInfo info = sweetTrackerPort.getTrackingInfo(
                delivery.getTrackingInfo().carrierCode(),
                delivery.getTrackingInfo().trackingNumber()
        );

        if (info.details() == null || info.details().isEmpty()) {
            return;
        }

        List<TrackingHistory> histories = info.details().stream()
                .map(this::toTrackingHistory)
                .toList();

        if (info.complete()) {
            histories.subList(0, histories.size() - 1).forEach(delivery::addHistory);
            delivery.complete(histories.getLast());

            Instant now = Instant.now();
            deliveryOutboxPort.save(DeliveryCompletedEvent.builder()
                    .orderId(delivery.getOrderId())
                    .orderLineId(delivery.getOrderLineId())
                    .deliveryId(delivery.getId().value())
                    .trackingNumber(delivery.getTrackingInfo().trackingNumber())
                    .deliveredAt(now)
                    .occurredAt(now)
                    .build());

            log.info("[DeliverySync] 배송 완료. deliveryId={}, orderLineId={}", delivery.getId().value(), delivery.getOrderLineId());
        }
        else if (delivery.getStatus() == DeliveryStatus.SHIPPED) {
            delivery.transit(histories.getFirst());
            histories.subList(1, histories.size()).forEach(delivery::addHistory);
        }
        else {
            histories.forEach(delivery::addHistory);
        }

        deliveryRepositoryPort.update(delivery);
    }

    private TrackingHistory toTrackingHistory(SweetTrackerTrackingInfo.TrackingDetail detail) {
        return TrackingHistory.createBuilder()
                .status(resolveStatus(detail.level()))
                .description(buildDescription(detail))
                .trackedAt(parseTime(detail.time()))
                .build();
    }

    private TrackingHistoryStatus resolveStatus(Integer level) {
        if (level == null) return TrackingHistoryStatus.IN_TRANSIT;
        if (level == sweetTrackerProperties.getDeliveryCompleteLevel()) return TrackingHistoryStatus.DELIVERED;
        if (level == 1) return TrackingHistoryStatus.SHIPPED;
        return TrackingHistoryStatus.IN_TRANSIT;
    }

    private String buildDescription(SweetTrackerTrackingInfo.TrackingDetail detail) {
        return Stream.of(detail.where(), detail.kind())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" - ", "", ""))
                .transform(s -> s.isBlank() ? "배송 정보가 업데이트되었습니다." : s);
    }

    private Instant parseTime(String timeString) {
        return Optional.ofNullable(timeString)
                .filter(s -> !s.isBlank())
                .map(s -> LocalDateTime.parse(s, SWEET_TRACKER_FORMAT)
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toInstant())
                .orElse(null);
    }
}
