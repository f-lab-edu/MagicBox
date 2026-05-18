package kr.magicbox.subscribe.application.dto.query;

import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

public record IsSubscribedQuery(CreatorId creatorId, SubscriberId subscriberId) {
    public static IsSubscribedQuery of(CreatorId creatorId, SubscriberId subscriberId) {
        return new IsSubscribedQuery(creatorId, subscriberId);
    }
}
