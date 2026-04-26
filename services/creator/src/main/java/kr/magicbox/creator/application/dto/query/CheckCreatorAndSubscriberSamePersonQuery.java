package kr.magicbox.creator.application.dto.query;

import kr.magicbox.creator.domain.vo.CreatorId;
import kr.magicbox.creator.domain.vo.UserId;

public record CheckCreatorAndSubscriberSamePersonQuery(CreatorId creatorId, UserId subscriberId) {
    public static CheckCreatorAndSubscriberSamePersonQuery of(CreatorId creatorId, UserId subscriberId) {
        return new CheckCreatorAndSubscriberSamePersonQuery(creatorId, subscriberId);
    }
}
