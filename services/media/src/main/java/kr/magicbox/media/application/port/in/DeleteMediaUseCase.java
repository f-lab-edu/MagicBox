package kr.magicbox.media.application.port.in;

import kr.magicbox.media.domain.vo.UserId;

public interface DeleteMediaUseCase {
    void delete(String uuid, UserId requesterId);
    void deleteAsAdmin(String uuid);
}
