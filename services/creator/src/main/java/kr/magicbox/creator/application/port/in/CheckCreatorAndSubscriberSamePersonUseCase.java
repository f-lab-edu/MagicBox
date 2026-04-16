package kr.magicbox.creator.application.port.in;

import kr.magicbox.creator.application.dto.query.CheckCreatorAndSubscriberSamePersonQuery;

public interface CheckCreatorAndSubscriberSamePersonUseCase {
    boolean isSamePerson(CheckCreatorAndSubscriberSamePersonQuery query);
}
