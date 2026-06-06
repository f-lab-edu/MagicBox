package kr.magicbox.media.application.service;

import kr.magicbox.media.application.port.in.CleanupInactiveMediaUseCase;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.application.port.out.ObjectStoragePort;
import kr.magicbox.media.domain.aggregate.Media;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupInactiveMediaService implements CleanupInactiveMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final ObjectStoragePort objectStoragePort;

    @Value("${cleanup.inactive-media-age-hours}")
    private long inactiveMediaAgeHours;

    @Transactional
    @Override
    public void cleanupInactiveMedia() {
        Instant threshold = Instant.now().minus(inactiveMediaAgeHours, ChronoUnit.HOURS);
        List<Media> orphans = mediaRepositoryPort.findInactiveOlderThan(threshold);

        if (orphans.isEmpty()) {
            return;
        }

        log.info("[Cleanup] 고아 미디어 {} 건 정리 시작", orphans.size());

        for (Media media : orphans) {
            objectStoragePort.deleteIfExists(media.getUuid());
        }

        List<String> uuids = orphans.stream().map(Media::getUuid).toList();
        mediaRepositoryPort.deleteAllByUuids(uuids);

        log.info("[Cleanup] 고아 미디어 {} 건 정리 완료", orphans.size());
    }
}
