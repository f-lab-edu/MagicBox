package kr.magicbox.release.application.port.in;

import kr.magicbox.release.domain.vo.ReleaseId;

public interface IncreaseSoldQuantityUseCase {
    void increaseSoldQuantity(ReleaseId releaseId);
}
