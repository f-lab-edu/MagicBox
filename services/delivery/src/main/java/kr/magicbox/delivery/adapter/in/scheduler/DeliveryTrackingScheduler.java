package kr.magicbox.delivery.adapter.in.scheduler;

import kr.magicbox.delivery.application.port.out.DeliveryRepositoryPort;
import kr.magicbox.delivery.application.service.SyncDeliveryTrackingService;
import kr.magicbox.delivery.domain.aggregate.Delivery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryTrackingScheduler {

    private final DeliveryRepositoryPort deliveryRepositoryPort;
    private final SyncDeliveryTrackingService syncDeliveryTrackingService;

    @Scheduled(fixedDelay = 3 * 60 * 60 * 1000L)
    public void syncDeliveryTracking() {
        List<Delivery> inProgress = deliveryRepositoryPort.findAllInProgress();
        log.info("[DeliveryScheduler] 배송 추적 동기화 시작. 대상 건수={}", inProgress.size());
        inProgress.forEach(syncDeliveryTrackingService::syncTracking);
        log.info("[DeliveryScheduler] 배송 추적 동기화 완료.");
    }
}
