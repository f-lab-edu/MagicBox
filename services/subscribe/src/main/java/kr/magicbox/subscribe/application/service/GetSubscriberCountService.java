package kr.magicbox.subscribe.application.service;

import kr.magicbox.subscribe.application.dto.query.GetSubscriberCountQuery;
import kr.magicbox.subscribe.application.port.in.GetSubscriberCountUseCase;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetSubscriberCountService implements GetSubscriberCountUseCase {
    private final SubscriptionRepositoryPort subscriptionRepositoryPort;

    @Transactional(readOnly = true)
    @Override
    public long getSubscriberCount(GetSubscriberCountQuery query) {
        return subscriptionRepositoryPort.countByCreatorId(query.creatorId());
    }
}
