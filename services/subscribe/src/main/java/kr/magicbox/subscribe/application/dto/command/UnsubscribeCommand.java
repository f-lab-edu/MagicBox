package kr.magicbox.subscribe.application.dto.command;

import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

public record UnsubscribeCommand(SubscriberId subscriberId, CreatorId creatorId) {
    public static UnsubscribeCommand of(SubscriberId subscriberId, CreatorId creatorId) {
        return new UnsubscribeCommand(subscriberId, creatorId);
    }
}
