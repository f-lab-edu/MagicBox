package kr.magicbox.subscribe.application.dto.query;

import kr.magicbox.subscribe.domain.vo.CreatorId;

public record GetSubscriberCountQuery(CreatorId creatorId) {
    public static GetSubscriberCountQuery of(CreatorId creatorId) {
        return new GetSubscriberCountQuery(creatorId);
    }
}
