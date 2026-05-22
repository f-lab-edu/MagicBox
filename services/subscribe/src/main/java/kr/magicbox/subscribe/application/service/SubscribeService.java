package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.command.SubscribeCommand;
import kr.magicbox.subscribe.application.port.in.SubscribeUseCase;
import kr.magicbox.subscribe.application.port.out.CreatorIdentityQueryPort;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import kr.magicbox.subscribe.domain.aggregate.Subscription;
import kr.magicbox.subscribe.domain.exception.AlreadySubscribedException;
import kr.magicbox.subscribe.domain.exception.SelfSubscriptionNotAllowedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService implements SubscribeUseCase {
    private final CreatorIdentityQueryPort creatorIdentityQueryPort;
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional
    @Override
    public void subscribe(SubscribeCommand command) {
        if (creatorIdentityQueryPort.isCreatorOwnedByUser(command.creatorId(), command.subscriberId())) {
            throw new SelfSubscriptionNotAllowedException();
        }

        if (subscriptionRepositoryPort.existsBySubscriberIdAndCreatorId(command.subscriberId(), command.creatorId())) {
            throw new AlreadySubscribedException();
        }

        Subscription subscription = Subscription.createBuilder()
                .subscriberId(command.subscriberId())
                .creatorId(command.creatorId())
                .build();

        subscriptionRepositoryPort.save(subscription);
    }
}
