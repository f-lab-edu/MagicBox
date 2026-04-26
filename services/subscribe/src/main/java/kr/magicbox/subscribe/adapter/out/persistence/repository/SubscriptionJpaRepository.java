package kr.magicbox.subscribe.adapter.out.persistence.repository;

import kr.magicbox.subscribe.adapter.out.persistence.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    boolean existsBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);

    long countByCreatorId(Long creatorId);

    void deleteBySubscriberIdAndCreatorId(Long subscriberId, Long creatorId);

    void deleteAllBySubscriberId(Long subscriberId);

    void deleteAllByCreatorId(Long creatorId);
}
