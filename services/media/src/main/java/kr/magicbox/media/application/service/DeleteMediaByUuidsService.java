package kr.magicbox.media.application.service;

import kr.magicbox.media.application.port.in.DeleteMediaByUuidsUseCase;
import kr.magicbox.media.application.port.out.MediaRepositoryPort;
import kr.magicbox.media.application.port.out.ObjectStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMediaByUuidsService implements DeleteMediaByUuidsUseCase {

    private final MediaRepositoryPort mediaRepositoryPort;
    private final ObjectStoragePort objectStoragePort;

    @Transactional
    @Override
    public void deleteByUuids(List<String> uuids) {
        for (String uuid : uuids) {
            objectStoragePort.deleteIfExists(uuid);
        }
        mediaRepositoryPort.deleteAllByUuids(uuids);
        log.debug("[Media] uuid {} 파일 즉시 삭제 완료", uuids);
    }
}
