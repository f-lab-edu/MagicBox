package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.HandleCreatorRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleCreatorRevokedUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleCreatorRevokedService implements HandleCreatorRevokedUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void handleCreatorRevoked(HandleCreatorRevokedCommand command) {
        subscriptionRepositoryPort.deleteAllByCreatorId(command.creatorId());
    }
}
