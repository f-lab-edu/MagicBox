package kr.magicbox.auth.application.port.in;

import kr.magicbox.auth.domain.vo.UserId;

public interface HandleUserBannedUseCase {
    void handleUserBanned(UserId userId);
}
