package kr.magicbox.subscribe.adapter.out.persistence;

import kr.magicbox.subscribe.adapter.out.persistence.mapper.SubscriptionMapper;
import kr.magicbox.subscribe.adapter.out.persistence.repository.SubscriptionJpaRepository;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import kr.magicbox.subscribe.domain.aggregate.Subscription;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionJpaAdapter implements SubscriptionRepositoryPort {
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public void save(Subscription subscription) {
        subscriptionJpaRepository.save(subscriptionMapper.toEntity(subscription));
    }

    @Override
    public void deleteBySubscriberIdAndCreatorId(SubscriberId subscriberId, CreatorId creatorId) {
        subscriptionJpaRepository.deleteBySubscriberIdAndCreatorId(subscriberId.value(), creatorId.value());
    }

    @Override
    public void deleteAllBySubscriberId(SubscriberId subscriberId) {
        subscriptionJpaRepository.deleteAllBySubscriberId(subscriberId.value());
    }

    @Override
    public void deleteAllByCreatorId(CreatorId creatorId) {
        subscriptionJpaRepository.deleteAllByCreatorId(creatorId.value());
    }

    @Override
    public boolean existsBySubscriberIdAndCreatorId(SubscriberId subscriberId, CreatorId creatorId) {
        return subscriptionJpaRepository.existsBySubscriberIdAndCreatorId(subscriberId.value(), creatorId.value());
    }

    @Override
    public long countByCreatorId(CreatorId creatorId) {
        return subscriptionJpaRepository.countByCreatorId(creatorId.value());
    }
}
