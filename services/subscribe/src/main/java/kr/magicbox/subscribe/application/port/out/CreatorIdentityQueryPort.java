package kr.magicbox.subscribe.application.port.out;

import kr.magicbox.subscribe.domain.vo.CreatorId;
import kr.magicbox.subscribe.domain.vo.SubscriberId;

public interface CreatorIdentityQueryPort {
    boolean isCreatorOwnedByUser(CreatorId creatorId, SubscriberId subscriberId);
}
