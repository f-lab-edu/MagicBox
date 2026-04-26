package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.in.HandleUserBannedUseCase;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandleUserBannedService implements HandleUserBannedUseCase {
    private final InvalidateUserAuthStateService invalidateUserAuthStateService;

    @Override
    public void handleUserBanned(UserId userId) {
        invalidateUserAuthStateService.invalidate(userId);
    }
}
