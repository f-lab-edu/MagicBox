package kr.magicbox.release.application.service;

import kr.magicbox.release.application.port.in.IncreaseSoldQuantityUseCase;
import kr.magicbox.release.application.port.out.ReleaseRepositoryPort;
import kr.magicbox.release.domain.aggregate.Release;
import kr.magicbox.release.domain.vo.ReleaseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IncreaseSoldQuantityService implements IncreaseSoldQuantityUseCase {

    private final ReleaseRepositoryPort releaseRepositoryPort;

    @Override
    @Transactional
    public void increaseSoldQuantity(ReleaseId releaseId) {
        Release release = releaseRepositoryPort.findById(releaseId);
        release.increaseSoldQuantity();
        releaseRepositoryPort.update(release);
    }
}
