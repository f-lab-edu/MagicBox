package kr.magicbox.release.adapter.in.scheduler;

import kr.magicbox.release.application.port.in.AutoStartSaleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartSaleScheduler {

    private final AutoStartSaleUseCase autoStartSaleUseCase;

    @Scheduled(fixedDelay = 60000)
    public void autoStartScheduledReleases() {
        log.info("[Scheduler] 판매 예정 릴리즈 자동 오픈 시작");
        autoStartSaleUseCase.autoStartScheduledReleases();
        log.info("[Scheduler] 판매 예정 릴리즈 자동 오픈 완료");
    }
}
