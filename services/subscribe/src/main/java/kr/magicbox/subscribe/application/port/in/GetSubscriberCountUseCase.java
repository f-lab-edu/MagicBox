package kr.magicbox.subscribe.application.port.in;

import kr.magicbox.subscribe.application.dto.query.GetSubscriberCountQuery;

public interface GetSubscriberCountUseCase {
    long getSubscriberCount(GetSubscriberCountQuery query);
}
