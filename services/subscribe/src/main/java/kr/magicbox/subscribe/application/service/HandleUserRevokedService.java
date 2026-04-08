package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.HandleUserRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleUserRevokedUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleUserRevokedService implements HandleUserRevokedUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void handleUserRevoked(HandleUserRevokedCommand command) {
        if (subscriptionRepositoryPort.findAllBySubscriberId(command.subscriberId()).isEmpty()) {
            return;
        }
        subscriptionRepositoryPort.deleteAllBySubscriberId(command.subscriberId());
    }
}
