package kr.magicbox.subscribe.domain.aggregate;

import kr.magicbox.subscribe.domain.exception.InvalidFieldException;
import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;
import kr.magicbox.subscribe.domain.vo.SubscriptionId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Subscription {
    private final SubscriptionId id;
    private final SubscriberId subscriberId;
    private final CreatorId creatorId;

    @Builder(builderMethodName = "createBuilder", builderClassName = "CreateBuilder")
    public Subscription(SubscriberId subscriberId, CreatorId creatorId) {
        validateFields(subscriberId, creatorId);
        this.id = null;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
    }

    @Builder(builderMethodName = "reconstructBuilder", builderClassName = "ReconstructBuilder")
    public Subscription(SubscriptionId id, SubscriberId subscriberId, CreatorId creatorId) {
        validateFields(subscriberId, creatorId);
        this.id = id;
        this.subscriberId = subscriberId;
        this.creatorId = creatorId;
    }

    public Long getCreatorIdValue() {
        return this.creatorId.value();
    }

    private void validateFields(SubscriberId subscriberId, CreatorId creatorId) {
        if (subscriberId == null) {
            throw new InvalidFieldException("구독자 ID는 필수 값입니다.");
        }
        if (creatorId == null) {
            throw new InvalidFieldException("크리에이터 ID는 필수 값입니다.");
        }
    }
}
