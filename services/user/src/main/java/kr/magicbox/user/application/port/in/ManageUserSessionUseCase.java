package kr.magicbox.user.application.port.in;

import kr.magicbox.user.domain.vo.UserId;

import java.time.Instant;

public interface ManageUserSessionUseCase {
    void startSession(UserId userId, Instant loginAt);
    void endSession(UserId userId, Instant logoutAt);
}