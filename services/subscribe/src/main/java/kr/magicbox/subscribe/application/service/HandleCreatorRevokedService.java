package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.HandleCreatorRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleCreatorRevokedUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleCreatorRevokedService implements HandleCreatorRevokedUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void handleCreatorRevoked(HandleCreatorRevokedCommand command) {
        int deleted = subscriptionRepositoryPort.deleteAllByCreatorId(command.creatorId());
        log.debug("구독 삭제 완료: creatorId={}, deletedCount={}", command.creatorId().value(), deleted);
    }
}