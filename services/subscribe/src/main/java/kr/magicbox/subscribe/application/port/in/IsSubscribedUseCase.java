package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.query.IsSubscribedQuery;

public interface IsSubscribedUseCase {
    boolean isSubscribed(IsSubscribedQuery query);
}
