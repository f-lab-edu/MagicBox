package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.AutoStartSaleUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoStartSaleService implements AutoStartSaleUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional
    public void autoStartScheduledReleases() {
        List<Release> releases = releaseRepositoryPort.findScheduledBefore(Instant.now());
        releases.forEach(release -> {
            release.startSale();
            releaseRepositoryPort.update(release);
        });
    }
}
