package kr.magicbox.subscribe.adapter.out.persistence.mapper;

import kr.magicbox.subscribe.adapter.out.persistence.entity.SubscriptionEntity;
import kr.magicbox.subscribe.domain.aggregate.Subscription;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.domain.vo.SubscriptionId;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    public SubscriptionEntity toEntity(Subscription domain) {
        return SubscriptionEntity.builder()
                .subscriberId(domain.getSubscriberId().value())
                .creatorId(domain.getCreatorIdValue())
                .build();
    }

    public Subscription toDomain(SubscriptionEntity entity) {
        return Subscription.reconstructBuilder()
                .id(SubscriptionId.of(entity.getId()))
                .subscriberId(SubscriberId.of(entity.getSubscriberId()))
                .creatorId(CreatorId.of(entity.getCreatorId()))
                .build();
    }
}
