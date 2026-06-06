package kr.magicbox.media.application.service;

import kr.magicbox.media.application.port.in.DeleteMediaUseCase;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.application.port.out.ObjectStoragePort;
import kr.magicbox.media.domain.aggregate.Media;
import kr.magicbox.media.domain.exception.MediaNotFoundException;
import kr.magicbox.media.domain.exception.MediaUnauthorizedException;
import kr.magicbox.media.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMediaService implements DeleteMediaUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final ObjectStoragePort objectStoragePort;

    @Transactional
    @Override
    public void delete(String uuid, UserId requesterId) {
        Media media = mediaRepositoryPort.findByUuid(uuid)
                .orElseThrow(MediaNotFoundException::new);

        if (!media.getUploaderId().equals(requesterId)) {
            throw new MediaUnauthorizedException();
        }

        deleteFromStorageAndDb(uuid);
        log.debug("[Media] uuid={} 삭제 완료 (uploaderId={})", uuid, requesterId.value());
    }

    @Transactional
    @Override
    public void deleteAsAdmin(String uuid) {
        mediaRepositoryPort.findByUuid(uuid)
                .orElseThrow(MediaNotFoundException::new);

        deleteFromStorageAndDb(uuid);
        log.debug("[Media] uuid={} 관리자 삭제 완료", uuid);
    }

    private void deleteFromStorageAndDb(String uuid) {
        objectStoragePort.deleteIfExists(uuid);
        mediaRepositoryPort.deleteByUuid(uuid);
    }
}
