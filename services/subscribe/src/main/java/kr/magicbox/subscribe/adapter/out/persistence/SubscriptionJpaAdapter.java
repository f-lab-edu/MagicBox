package kr.magicbox.subscribe.adapter.out.persistence;

import kr.magicbox.subscribe.adapter.out.persistence.mapper.SubscriptionMapper;
import kr.magicbox.subscribe.adapter.out.persistence.repository.SubscriptionJpaRepository;
import kr.magicbox.subscribe.application.port.out.SubscriptionRepositoryPort;
import kr.magicbox.subscribe.domain.aggregate.Subscription;
import kr.magicbox.subscribe.domain.exception.AlreadySubscribedException;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionJpaAdapter implements SubscriptionRepositoryPort {
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public void save(Subscription subscription) {
        try {
            subscriptionJpaRepository.save(subscriptionMapper.toEntity(subscription));
        } 
        catch (DataIntegrityViolationException e) {
            throw new AlreadySubscribedException();
        }
    }

    @Override
    public void deleteBySubscriberIdAndCreatorId(SubscriberId subscriberId, CreatorId creatorId) {
        subscriptionJpaRepository.deleteBySubscriberIdAndCreatorId(subscriberId.value(), creatorId.value());
    }

    @Override
    public int deleteAllBySubscriberId(SubscriberId subscriberId) {
        return subscriptionJpaRepository.deleteAllBySubscriberId(subscriberId.value());
    }

    @Override
    public int deleteAllByCreatorId(CreatorId creatorId) {
        return subscriptionJpaRepository.deleteAllByCreatorId(creatorId.value());
    }

    @Override
    public List<Subscription> findAllBySubscriberId(SubscriberId subscriberId) {
        return subscriptionJpaRepository.findAllBySubscriberId(subscriberId.value())
                .stream()
                .map(subscriptionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Subscription> findAllByCreatorId(CreatorId creatorId) {
        return subscriptionJpaRepository.findAllByCreatorId(creatorId.value())
                .stream()
                .map(subscriptionMapper::toDomain)
                .toList();
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
