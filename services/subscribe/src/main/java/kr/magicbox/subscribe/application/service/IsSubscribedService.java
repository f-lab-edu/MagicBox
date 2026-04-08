package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.query.IsSubscribedQuery;
import kr.magicbox.subscribe.application.port.in.IsSubscribedUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IsSubscribedService implements IsSubscribedUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional(readOnly = true)
    @Override
    public boolean isSubscribed(IsSubscribedQuery query) {
        return subscriptionRepositoryPort.existsBySubscriberIdAndCreatorId(query.subscriberId(), query.creatorId());
    }
}
