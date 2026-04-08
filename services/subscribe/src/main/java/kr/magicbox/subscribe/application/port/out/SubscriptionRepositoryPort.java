package kr.magicbox.subscribe.application.port.out;

import kr.magicbox.subscribe.domain.aggregate.Subscription;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

import java.util.List;

public interface SubscriptionRepositoryPort {
    void save(Subscription subscription);

    void deleteBySubscriberIdAndCreatorId(SubscriberId subscriberId, CreatorId creatorId);

    void deleteAllBySubscriberId(SubscriberId subscriberId);

    void deleteAllByCreatorId(CreatorId creatorId);

    List<Subscription> findAllBySubscriberId(SubscriberId subscriberId);

    List<Subscription> findAllByCreatorId(CreatorId creatorId);

    boolean existsBySubscriberIdAndCreatorId(SubscriberId subscriberId, CreatorId creatorId);

    long countByCreatorId(CreatorId creatorId);
}
