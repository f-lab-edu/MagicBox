package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.UnsubscribeCommand;
import kr.magicbox.subscribe.application.port.in.UnsubscribeUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import kr.magicbox.subscribe.domain.exception.SubscriptionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnsubscribeService implements UnsubscribeUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void unsubscribe(UnsubscribeCommand command) {
        if (!subscriptionRepositoryPort.existsBySubscriberIdAndCreatorId(command.subscriberId(), command.creatorId())) {
            throw new SubscriptionNotFoundException();
        }
        subscriptionRepositoryPort.deleteBySubscriberIdAndCreatorId(command.subscriberId(), command.creatorId());
    }
}
