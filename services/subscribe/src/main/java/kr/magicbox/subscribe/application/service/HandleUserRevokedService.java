package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.HandleUserRevokedCommand;
import kr.magicbox.subscribe.application.port.in.HandleUserRevokedUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HandleUserRevokedService implements HandleUserRevokedUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void handleUserRevoked(HandleUserRevokedCommand command) {
        int deleted = subscriptionRepositoryPort.deleteAllBySubscriberId(command.subscriberId());
        log.debug("구독 삭제 완료: subscriberId={}, deletedCount={}", command.subscriberId().value(), deleted);
    }
}