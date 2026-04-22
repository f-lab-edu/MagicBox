package kr.magicbox.subscribe.application.dto.command;

import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

public record SubscribeCommand(SubscriberId subscriberId, CreatorId creatorId) {
    public static SubscribeCommand of(SubscriberId subscriberId, CreatorId creatorId) {
        return new SubscribeCommand(subscriberId, creatorId);
    }
}
