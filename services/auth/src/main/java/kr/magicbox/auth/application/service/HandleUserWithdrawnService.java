package kr.magicbox.auth.application.service;

import kr.magicbox.auth.application.port.in.HandleUserWithdrawnUseCase;
import kr.magicbox.auth.domain.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandleUserWithdrawnService implements HandleUserWithdrawnUseCase {
    private final InvalidateUserAuthStateService invalidateUserAuthStateService;

    @Override
    public void handleUserWithdrawn(UserId userId) {
        invalidateUserAuthStateService.invalidate(userId);
    }
}
