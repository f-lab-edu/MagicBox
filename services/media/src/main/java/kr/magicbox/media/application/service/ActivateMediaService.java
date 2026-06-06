package kr.magicbox.media.application.service;

import kr.magicbox.media.application.port.in.ActivateMediaUseCase;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.domain.aggregate.Media;
import kr.magicbox.media.domain.enums.MediaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivateMediaService implements ActivateMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;

    @Transactional
    @Override
    public void activateByUuids(List<String> uuids) {
        List<Media> mediaList = mediaRepositoryPort.findAllByUuids(uuids);

        for (Media media : mediaList) {
            if (media.getStatus() == MediaStatus.ACTIVE) {
                continue;
            }
            media.activate();
            mediaRepositoryPort.update(media);
        }

        List<String> foundUuids = mediaList.stream().map(Media::getUuid).toList();
        uuids.stream()
                .filter(uuid -> !foundUuids.contains(uuid))
                .forEach(uuid -> log.warn("[Media] 활성화 대상 미디어 없음. uuid={}", uuid));
    }
}
